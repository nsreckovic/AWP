package com.ns.awp.ticket.service.impl;

import com.ns.awp.flightInstance.models.FlightInstance;
import com.ns.awp.flightInstance.repository.FlightInstanceRepository;
import com.ns.awp.ticket.models.Ticket;
import com.ns.awp.ticket.models.dto.*;
import com.ns.awp.ticket.repository.TicketRepository;
import com.ns.awp.ticket.service.TicketService;
import com.ns.awp.user.models.User;
import com.ns.awp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final FlightInstanceRepository flightInstanceRepository;
    private final UserRepository userRepository;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Override
    @Transactional
    public ResponseEntity<?> newTicket(TicketSaveRequestDto ticket) {
        try {
            // Flight Instance check
            FlightInstance flightInstance;
            if (!flightInstanceRepository.existsById(ticket.getFlightInstanceId())) {
                return ResponseEntity.status(404).body("Flight instance not found.");
            } else {
                flightInstance = flightInstanceRepository.findById(ticket.getFlightInstanceId()).get();
            }

            // Count check
            if (flightInstance.getCount() < 1) {
                return ResponseEntity.status(400).body("Maximum flight capacity reached.");
            } else {
                flightInstance.decrementCount();
            }

            // User check
            User user;
            if (!userRepository.existsById(ticket.getUserId())) {
                return ResponseEntity.status(404).body("User with provided id not found.");
            } else {
                user = userRepository.findById(ticket.getUserId()).get();
            }

            // Save
            flightInstanceRepository.save(flightInstance);
            Ticket saved = ticketRepository.save(new Ticket(
                    -1,
                    flightInstance,
                    user,
                    null,
                    null
            ));

            return ResponseEntity.ok(new TicketResponseDto(saved));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> newBulkTicket(BulkTicketSaveRequestDto bulkRequest) {
        try {
            // Flight Instance check
            FlightInstance flightInstance;
            if (!flightInstanceRepository.existsById(bulkRequest.getFlightInstanceId())) {
                return ResponseEntity.status(404).body("Flight instance not found.");
            } else {
                flightInstance = flightInstanceRepository.findById(bulkRequest.getFlightInstanceId()).get();
            }

            // Count check
            if (flightInstance.getCount() - bulkRequest.getCount() < 0) {
                return ResponseEntity.status(400).body("Requested number of tickets exceeds maximum capacity of " + flightInstance.getCount() + " tickets.");
            } else {
                flightInstance.bulkDecrementCount(bulkRequest.getCount());
            }

            // Save
            flightInstanceRepository.save(flightInstance);
            List<TicketResponseDto> saved = new ArrayList<>();
            for (int i = 0; i < bulkRequest.getCount(); i++) {
                Ticket t = new Ticket(
                        -1,
                        flightInstance,
                        null,
                        null,
                        null
                );
                saved.add(new TicketResponseDto(ticketRepository.save(t)));
            }

            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateTicket(TicketSaveRequestDto ticket) {
        try {
            // Ticket id check
            Ticket existing;
            if (ticket.getId() == null) {
                return ResponseEntity.status(400).body("Ticket id cannot be null.");
            } else if (!ticketRepository.existsById(ticket.getId())) {
                return ResponseEntity.status(404).body("Ticket with provided id not found.");
            } else {
                existing = ticketRepository.findById(ticket.getId()).get();
            }

            // Flight Instance check
            FlightInstance oldFlightInstance = null, newFlightInstance = null;
            if (ticket.getFlightInstanceId() != existing.getFlightInstance().getId()) {
                if (!flightInstanceRepository.existsById(ticket.getFlightInstanceId())) {
                    return ResponseEntity.status(404).body("Flight instance not found.");
                } else {
                    oldFlightInstance = existing.getFlightInstance();
                    newFlightInstance = flightInstanceRepository.findById(ticket.getFlightInstanceId()).get();
                    existing.setFlightInstance(newFlightInstance);
                }
            }

            // Count check
            if (newFlightInstance != null) {
                if (newFlightInstance.getCount() < 1) {
                    return ResponseEntity.status(400).body("Maximum flight capacity reached.");
                } else {
                    oldFlightInstance.incrementCount();
                    newFlightInstance.decrementCount();
                }
            }

            // User check
            if (ticket.getUserId() != existing.getUser().getId()) {
                if (!userRepository.existsById(ticket.getUserId())) {
                    return ResponseEntity.status(404).body("User with provided id not found.");
                } else {
                    existing.setUser(userRepository.findById(ticket.getUserId()).get());
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

    @Override
    public ResponseEntity<?> getAllTickets() {
        try {
            // Get all
            List<TicketResponseDto> tickets = new ArrayList<>();
            ticketRepository.findAll().forEach(ticket -> tickets.add(new TicketResponseDto(ticket)));

            return ResponseEntity.ok(tickets);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    @Override
    public ResponseEntity<?> getAllTicketsByUser(int userId) {
        try {
            // Get all from user with provided id
            List<TicketResponseDto> tickets = new ArrayList<>();
            ticketRepository.findAllByUserId(userId).forEach(ticket -> tickets.add(new TicketResponseDto(ticket)));

            return ResponseEntity.ok(tickets);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    @Override
    public ResponseEntity<?> getAllTicketsByAirline(int airlineId) {
        try {
            // Get all from airline with provided id
            List<TicketResponseDto> tickets = new ArrayList<>();
            ticketRepository.findAllByAirlineId(airlineId).forEach(ticket -> tickets.add(new TicketResponseDto(ticket)));

            return ResponseEntity.ok(tickets);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    @Override
    public ResponseEntity<?> getAllFromTicketsByFilter(Filter filter) {
        try {
            // Get filtered
            List<TicketResponseDto> tickets = new ArrayList<>();
            ticketRepository.findAllTicketsByFilter(
                    filter.getUserId(),
                    filter.getFromDate() != null ? new Timestamp(filter.getFromDate()) : null,
                    filter.getToDate() != null ? new Timestamp(filter.getToDate()) : null,
                    filter.getFromAirportId(),
                    filter.getToAirportId(),
                    filter.getAirlineId()).forEach(ticket -> tickets.add(new TicketResponseDto(ticket)));

            return ResponseEntity.ok(tickets);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    @Override
    public ResponseEntity<?> getAllReturnTicketsByFilter(Filter filter) {
        try {
            if (filter.getCurrentFromTicketId() == null) {
                return ResponseEntity.status(400).body("You must provide from ticket id.");
            }

            // Id check
            Ticket fromTicket;
            if (!ticketRepository.existsById(filter.getCurrentFromTicketId())) {
                return ResponseEntity.status(404).body("Ticket not found.");
            } else {
                fromTicket = ticketRepository.findById(filter.getCurrentFromTicketId()).get();
            }

            // Set flight from date (timestamp) for filter query
            Timestamp fromTimestamp = fromTicket.getFlightInstance().getFlightDate();
            fromTimestamp.setTime(
                    // Departure time
                    fromTimestamp.getTime() +
                    // Flight time
                    TimeUnit.MINUTES.toMillis(fromTicket.getFlightInstance().getFlightLengthInMinutes()) +
                    // Minimum time to switch flights
                    TimeUnit.MINUTES.toMillis(60)
            );

            // Get filtered tickets
            List<TicketResponseDto> tickets = new ArrayList<>();
            ticketRepository.findAllTicketsByFilter(
                    filter.getUserId(),
                    fromTimestamp,
                    filter.getToDate() != null ? new Timestamp(filter.getToDate()) : null,
                    fromTicket.getFlightInstance().getFlight().getArrivalAirport().getAirportId(),
                    fromTicket.getFlightInstance().getFlight().getDepartureAirport().getAirportId(),
                    filter.getAirlineId()).forEach(ticket -> tickets.add(new TicketResponseDto(ticket)));

            return ResponseEntity.ok(tickets);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    @Override
    public ResponseEntity<?> getTicketById(int id) {
        try {
            // Id check
            if (!ticketRepository.existsById(id)) {
                return ResponseEntity.status(404).body("Ticket not found.");
            }

            // Get by id
            TicketResponseDto ticket = new TicketResponseDto(ticketRepository.findById(id).get());

            return ResponseEntity.ok(ticket);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteTicketById(int id) {
        try {
            // Id check
            if (!ticketRepository.existsById(id)) {
                return ResponseEntity.status(404).body("Ticket not found.");
            }

            // Delete
            Ticket ticket = ticketRepository.findById(id).get();
            ticket.getFlightInstance().incrementCount();
            flightInstanceRepository.save(ticket.getFlightInstance());
            ticketRepository.deleteById(id);

            return ResponseEntity.ok("Ticket deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }
}
