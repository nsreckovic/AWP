package com.ns.awp.ticket.service;

import com.ns.awp.config.security.JsonMessage;
import com.ns.awp.config.security.JwtUtil;
import com.ns.awp.flightInstance.models.FlightInstance;
import com.ns.awp.flightInstance.repository.FlightInstanceRepository;
import com.ns.awp.ticket.models.Ticket;
import com.ns.awp.ticket.models.dto.*;
import com.ns.awp.ticket.repository.TicketRepository;
import com.ns.awp.user.models.User;
import com.ns.awp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final FlightInstanceRepository flightInstanceRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public ResponseEntity<?> newTicket(TicketRequestDto ticketRequest) {
        try {
            // Flight instance
            if (ticketRequest.getFlightInstanceId() == null) {
                return ResponseEntity.status(400).body("Flight instance id cannot be null.");
            }
            FlightInstance flightInstance;
            if (!flightInstanceRepository.existsById(ticketRequest.getFlightInstanceId())) {
                return ResponseEntity.status(404).body("Flight instance not found.");
            } else {
                flightInstance = flightInstanceRepository.findById(ticketRequest.getFlightInstanceId()).get();
            }

            // Count
            if (ticketRequest.getTicketCount() == null || ticketRequest.getTicketCount() < 1) {
                return ResponseEntity.status(400).body("Ticket count cannot be null and less than 1.");
            }
            if (flightInstance.getCount() - ticketRequest.getTicketCount() < 0) {
                return ResponseEntity.status(400).body("Requested number of tickets exceeds maximum capacity of flight's " + flightInstance.getCount() + " tickets.");
            } else {
                flightInstance.bulkDecrementCount(ticketRequest.getTicketCount());
            }

            // Save
            flightInstanceRepository.save(flightInstance);
            List<TicketResponseDto> saved = new ArrayList<>();
            for (int i = 0; i < ticketRequest.getTicketCount(); i++) {
                Ticket t = new Ticket(flightInstance);
                saved.add(new TicketResponseDto(ticketRepository.save(t)));
            }

            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    public ResponseEntity<?> updateTicket(TicketRequestDto ticket) {
        try {
            // Validation
            Ticket existing;
            if (ticket.getId() != null && ticketRepository.existsById(ticket.getId())) {
                existing = ticketRepository.findById(ticket.getId()).get();
            } else {
                return ResponseEntity.status(404).body("Ticket with provided id not found.");
            }

            // Flight instance
            FlightInstance oldFlightInstance = null, newFlightInstance = null;
            if (ticket.getFlightInstanceId() != null && ticket.getFlightInstanceId() != existing.getFlightInstance().getId()) {
                if (!flightInstanceRepository.existsById(ticket.getFlightInstanceId())) {
                    return ResponseEntity.status(404).body("Flight instance not found.");
                } else {
                    oldFlightInstance = existing.getFlightInstance();
                    newFlightInstance = flightInstanceRepository.findById(ticket.getFlightInstanceId()).get();
                    existing.setFlightInstance(newFlightInstance);
                }
            }

            // Count
            if (newFlightInstance != null) {
                if (newFlightInstance.getCount() < 1) {
                    return ResponseEntity.status(400).body("Flight instance with the requested id already has the maximum number of tickets.");
                } else {
                    oldFlightInstance.incrementCount();
                    newFlightInstance.decrementCount();
                }
            }

            // Save
            if (oldFlightInstance != null) flightInstanceRepository.save(oldFlightInstance);
            if (newFlightInstance != null) flightInstanceRepository.save(newFlightInstance);
            existing = ticketRepository.save(existing);

            return ResponseEntity.ok(new TicketResponseDto(existing));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    public ResponseEntity<?> getAllTickets(TicketFilter filter) {
        try {
            // JWT check
            User authenticated = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            if (filter.getUserId() != null) {
                if (jwtUtil.hasRole("ROLE_REGULAR") && authenticated.getId() != filter.getUserId()) {
                    return ResponseEntity.status(401).body("You cannot see another user's tickets.");
                }
            } else if (jwtUtil.hasRole("ROLE_REGULAR")) {
                filter.setUserId(authenticated.getId());
            }

            // Get filtered
            List<TicketResponseDto> tickets = new ArrayList<>();
            ticketRepository.findAllUserTicketsByFilter(
                    filter.getUserId(),
                    filter.getFromDate() != null ? new Timestamp(filter.getFromDate()) : null,
                    filter.getToDate() != null ? new Timestamp(filter.getToDate()) : null,
                    filter.getFromAirportId(),
                    filter.getToAirportId(),
                    filter.getAirlineId()).forEach(ticket -> tickets.add(new TicketResponseDto(ticket)));

            return ResponseEntity.ok(tickets);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    public ResponseEntity<?> getAllAvailableFromTicketsByFilter(TicketFilter filter) {
        try {
            // Get filtered
            Set<TicketWithoutUserResponseDto> tickets = new HashSet<>();
            ticketRepository.findAllAvailableTicketsByFilter(
                    filter.getFromDate() != null ? new Timestamp(filter.getFromDate()) : null,
                    filter.getToDate() != null ? new Timestamp(filter.getToDate()) : null,
                    filter.getFromAirportId(),
                    filter.getToAirportId(),
                    filter.getAirlineId()).forEach(ticket -> tickets.add(new TicketWithoutUserResponseDto(ticket)));

            return ResponseEntity.ok(tickets);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    public ResponseEntity<?> getAllAvailableReturnTicketsByFilter(TicketFilter filter) {
        try {
            // From ticket validation
            Ticket fromTicket;
            if (filter.getFromTicketId() != null && ticketRepository.existsById(filter.getFromTicketId())) {
                fromTicket = ticketRepository.findById(filter.getFromTicketId()).get();
            } else {
                return ResponseEntity.status(404).body("Ticket not found.");
            }

            // Set fromDate (timestamp) for filter query
            Timestamp fromTimestamp = fromTicket.getFlightInstance().getFlightDate();
            fromTimestamp.setTime(
                    // Departure time
                    fromTimestamp.getTime() +
                    // Flight time
                    TimeUnit.MINUTES.toMillis(fromTicket.getFlightInstance().getFlightLengthInMinutes()) +
                    // Minimum time to switch flights
                    TimeUnit.MINUTES.toMillis(60)
            );

            // Set airports for filter query
            Integer departureAirportId = fromTicket.getFlightInstance().getFlight().getArrivalAirport().getId();
            Integer arrivalAirportId = fromTicket.getFlightInstance().getFlight().getDepartureAirport().getId();

            // Get filtered tickets
            Set<TicketWithoutUserResponseDto> tickets = new HashSet<>();
            ticketRepository.findAllAvailableTicketsByFilter(
                    fromTimestamp,
                    filter.getToDate() != null ? new Timestamp(filter.getToDate()) : null,
                    departureAirportId,
                    arrivalAirportId,
                    filter.getAirlineId()).forEach(ticket -> tickets.add(new TicketWithoutUserResponseDto(ticket))
            );

            return ResponseEntity.ok(tickets);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    public ResponseEntity<?> getTicketById(int id) {
        try {
            // Validation
            if (!ticketRepository.existsById(id)) {
                return ResponseEntity.status(404).body("Ticket not found.");
            }

            // Get by id
            Ticket ticket = ticketRepository.findById(id).get();

            // JWT check
            User authenticated = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            if (jwtUtil.hasRole("ROLE_REGULAR") && authenticated.getId() != ticket.getUser().getId()) {
                return ResponseEntity.status(401).body("You cannot see another users' ticket.");
            }

            return ResponseEntity.ok(new TicketResponseDto(ticket));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    public ResponseEntity<?> deleteTicketById(int id) {
        try {
            // Validation
            if (!ticketRepository.existsById(id)) {
                return ResponseEntity.status(404).body("Ticket not found.");
            }

            // Delete
            Ticket ticket = ticketRepository.findById(id).get();
            ticket.getFlightInstance().incrementCount();
            flightInstanceRepository.save(ticket.getFlightInstance());
            ticketRepository.deleteById(id);

            return ResponseEntity.ok(new JsonMessage("Ticket deleted."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }
}
