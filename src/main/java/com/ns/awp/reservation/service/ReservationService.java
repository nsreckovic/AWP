package com.ns.awp.reservation.service;

import com.ns.awp.config.JsonMessage;
import com.ns.awp.config.JwtUtil;
import com.ns.awp.reservation.models.Reservation;
import com.ns.awp.reservation.models.dto.ReservationFilter;
import com.ns.awp.reservation.models.dto.ReservationResponseDto;
import com.ns.awp.reservation.models.dto.ReservationRequestDto;
import com.ns.awp.reservation.repository.ReservationRepository;
import com.ns.awp.ticket.models.Ticket;
import com.ns.awp.ticket.repository.TicketRepository;
import com.ns.awp.user.models.User;
import com.ns.awp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public ResponseEntity<?> newReservation(ReservationRequestDto reservation) {
        try {
            // Null check
            if (reservation.getDepartureTicketId() == null) {
                return ResponseEntity.status(400).body("Departure ticket id cannot be null.");
            } else if (reservation.getUserId() == null) {
                return ResponseEntity.status(400).body("User id cannot be null.");
            }

            // Reservation timestamp
            Timestamp reservationTimestamp = new Timestamp(new Date().getTime());

            // User check
            User user;
            if (!userRepository.existsById(reservation.getUserId())) {
                return ResponseEntity.status(404).body("User with provided id not found.");
            } else {
                user = userRepository.findById(reservation.getUserId()).get();
            }

            // Departure ticket
            Ticket departureTicket;
            if (!ticketRepository.existsById(reservation.getDepartureTicketId())) {
                return ResponseEntity.status(404).body("Ticket not found.");
            } else {
                departureTicket = ticketRepository.findById(reservation.getDepartureTicketId()).get();
                if (departureTicket.getUser() != null) {
                    return ResponseEntity.status(400).body("Departure ticket with provided id has already been assigned to the user.");
                } else {
                    if (reservationTimestamp.after(departureTicket.getFlightInstance().getFlightDate())) {
                        return ResponseEntity.status(400).body("Reservation cannot be made for a flight in the past.");
                    } else if (reservationTimestamp.after(new Timestamp(departureTicket.getFlightInstance().getFlightDate().getTime() - TimeUnit.DAYS.toMillis(1)))) {
                        return ResponseEntity.status(400).body("Reservation cannot be made 24 hours before the flight.");
                    } else {
                        departureTicket.setUser(user);
                    }
                }
            }

            // Return ticket
            Ticket returnTicket;
            if (reservation.getReturnTicketId() != null) {
                if (!ticketRepository.existsById(reservation.getReturnTicketId())) {
                    return ResponseEntity.status(404).body("Ticket not found.");
                } else {
                    returnTicket = ticketRepository.findById(reservation.getReturnTicketId()).get();
                    if (returnTicket.getUser() != null) {
                        return ResponseEntity.status(400).body("Return ticket with provided id has already been assigned to the user.");
                    } else {
                        returnTicket.setUser(user);
                    }
                }
            } else {
                returnTicket = null;
            }

            // Save
            ticketRepository.save(departureTicket);
            if (returnTicket != null) ticketRepository.save(returnTicket);

            Reservation saved = reservationRepository.save(new Reservation(
                -1,
                departureTicket,
                returnTicket,
                reservationTimestamp,
                user
            ));

            return ResponseEntity.ok(new ReservationResponseDto(saved));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    public ResponseEntity<?> updateReservation(ReservationRequestDto reservation) {
        try {
            // Null check
            Reservation existing;
            if (reservation.getId() == null) {
                return ResponseEntity.status(400).body("Reservation id cannot be null.");
            }
            if (!reservationRepository.existsById(reservation.getId())) {
                return ResponseEntity.status(404).body("Reservation with provided id not found.");
            }
            existing = reservationRepository.findById(reservation.getId()).get();



            // JWT check
            User authenticated = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            if (jwtUtil.hasRole("ROLE_REGULAR") && authenticated.getId() != existing.getUser().getId()) {
                return ResponseEntity.status(401).body("You cannot change another user's reservation.");
            }



            // Reservation timestamp
            Timestamp updateTimestamp = new Timestamp(new Date().getTime());



            // Time check
            if (jwtUtil.hasRole("ROLE_REGULAR")) {
                if (updateTimestamp.after(existing.getDepartureTicket().getFlightInstance().getFlightDate())) {
                    return ResponseEntity.status(400).body("Past reservations cannot be changed.");
                } else if (updateTimestamp.after(new Timestamp(existing.getDepartureTicket().getFlightInstance().getFlightDate().getTime() - TimeUnit.DAYS.toMillis(1)))) {
                    return ResponseEntity.status(400).body("Reservation cannot be updated 24 hours before the flight.");
                }
            }



            // User check
            User oldUser = null;
            if (reservation.getUserId() != null) {
                if (!userRepository.existsById(reservation.getUserId())) {
                    return ResponseEntity.status(404).body("User with provided id not found.");
                } else {
                    oldUser = existing.getUser();
                    existing.setUser(userRepository.findById(reservation.getUserId()).get());
                }
            }



            // Departure ticket
            Ticket oldDepartureTicket = null;
            Ticket newDepartureTicket = null;

            if (reservation.getDepartureTicketId() != null) {
                if (!ticketRepository.existsById(reservation.getDepartureTicketId())) {
                    return ResponseEntity.status(404).body("Ticket not found.");
                } else {
                    oldDepartureTicket = existing.getDepartureTicket();
                    newDepartureTicket = ticketRepository.findById(reservation.getDepartureTicketId()).get();

                    if (!oldDepartureTicket.equals(newDepartureTicket)) {
                        if (newDepartureTicket.getUser() != null) {
                            return ResponseEntity.status(400).body("Departure ticket with provided id has already been assigned to the user.");
                        } else {
                            if (updateTimestamp.after(newDepartureTicket.getFlightInstance().getFlightDate())) {
                                return ResponseEntity.status(400).body("Reservation cannot be updated for the flight in the past.");
                            } else if (updateTimestamp.after(new Timestamp(newDepartureTicket.getFlightInstance().getFlightDate().getTime() - TimeUnit.DAYS.toMillis(1)))) {
                                return ResponseEntity.status(400).body("Reservation cannot be updated for the flight that leaves in next 24 hours.");
                            } else {
                                // Reset old departure ticket
                                oldDepartureTicket.setUser(null);
                                // Set new departure ticket
                                newDepartureTicket.setUser(existing.getUser());
                                existing.setDepartureTicket(newDepartureTicket);
                            }
                        }

                    // If departure tickets are the same, set the departure tickets to null so that ticketRepository isn't unnecessary called
                    } else {
                        oldDepartureTicket = null;
                        newDepartureTicket = null;
                    }
                }

            // If departure ticket is not changing, check if user is going to be changed so it can be changed in the departure ticket as well
            } else if (oldUser != null && !oldUser.equals(existing.getUser())){
                existing.getDepartureTicket().setUser(existing.getUser());
                oldDepartureTicket = existing.getDepartureTicket();
            }



            // Return ticket
            Ticket oldReturnTicket = null;
            Ticket newReturnTicket = null;

            // -1 sets the return ticket to null, null ignores the return ticket setting and everything else is viewed as a return ticket id
            if (reservation.getReturnTicketId() != null && reservation.getReturnTicketId() == -1) {
                if (existing.getReturnTicket() != null) {
                    // Reset old return ticket
                    oldReturnTicket = existing.getReturnTicket();
                    oldReturnTicket.setUser(null);
                    // Set new return ticket
                    existing.setReturnTicket(null);
                }

            } else if (reservation.getReturnTicketId() != null && reservation.getReturnTicketId() != -1) {
                if (!ticketRepository.existsById(reservation.getReturnTicketId())) {
                    return ResponseEntity.status(404).body("Ticket not found.");
                } else {
                    oldReturnTicket = existing.getReturnTicket();
                    newReturnTicket = ticketRepository.findById(reservation.getReturnTicketId()).get();

                    if (!newReturnTicket.equals(oldReturnTicket)){
                        if (newReturnTicket.getUser() != null) {
                            return ResponseEntity.status(400).body("Return ticket with provided id has already been assigned to the user.");

                        } else if (newReturnTicket.getFlightInstance().getFlightDate().before(new Timestamp(existing.getDepartureTicket().getFlightInstance().getFlightDate().getTime() + TimeUnit.MINUTES.toMillis(existing.getDepartureTicket().getFlightInstance().getFlightLengthInMinutes()) + TimeUnit.MINUTES.toMillis(60)))) {
                            return ResponseEntity.status(400).body("Return ticket must be at least one hour after the departure flight has landed.");

                        } else if (!existing.getDepartureTicket().getFlightInstance().getFlight().getArrivalAirport().getId().equals(newReturnTicket.getFlightInstance().getFlight().getDepartureAirport().getId())) {
                            return ResponseEntity.status(400).body("Arrival airport of the departure ticket must be the same as the departure airport of the return ticket.");

                        } else if (!existing.getDepartureTicket().getFlightInstance().getFlight().getDepartureAirport().getId().equals(newReturnTicket.getFlightInstance().getFlight().getArrivalAirport().getId())) {
                            return ResponseEntity.status(400).body("Departure airport of the departure ticket must be the same as the arrival airport of the return ticket.");

                        } else {
                            // Reset old return ticket
                            if (oldReturnTicket != null) oldReturnTicket.setUser(null);
                            // Set new return ticket
                            newReturnTicket.setUser(existing.getUser());
                            existing.setReturnTicket(newReturnTicket);
                        }
                    } else {
                        oldReturnTicket = null;
                        newReturnTicket = null;
                    }
                }
            // If return ticket is not changing, check if user is going to be changed so it can be changed in the return ticket as well
            } else if (oldUser != null && !oldUser.equals(existing.getUser())){
                if (existing.getReturnTicket() != null) {
                    existing.getReturnTicket().setUser(existing.getUser());
                    oldReturnTicket = existing.getReturnTicket();
                }
            }



            // Save
            if (oldDepartureTicket != null) ticketRepository.save(oldDepartureTicket);
            if (newDepartureTicket != null) ticketRepository.save(newDepartureTicket);
            if (oldReturnTicket != null) ticketRepository.save(oldReturnTicket);
            if (newReturnTicket != null) ticketRepository.save(newReturnTicket);
            existing = reservationRepository.save(existing);



            return ResponseEntity.ok(new ReservationResponseDto(existing));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    public ResponseEntity<?> getAllReservations(ReservationFilter filter) {
        try {
            // JWT check
            User authenticated = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            if (filter.getUserId() != null) {
                if (jwtUtil.hasRole("ROLE_REGULAR") && authenticated.getId() != filter.getUserId()) {
                    return ResponseEntity.status(401).body("You cannot see another users' reservations.");
                }
            } else if (jwtUtil.hasRole("ROLE_REGULAR")) {
                filter.setUserId(authenticated.getId());
            }

            // Get all
            List<ReservationResponseDto> reservations = new ArrayList<>();
            reservationRepository.findAllUserReservationsByFilter(
                    filter.getUserId(),
                    filter.getFromDate() != null ? new Timestamp(filter.getFromDate()) : null,
                    filter.getToDate() != null ? new Timestamp(filter.getToDate()) : null,
                    filter.getFromAirportId(),
                    filter.getToAirportId(),
                    filter.getAirlineId()).forEach(reservation -> reservations.add(new ReservationResponseDto(reservation)));

            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    public ResponseEntity<?> getReservationById(int id) {
        try {
            // Id check
            if (!reservationRepository.existsById(id)) {
                return ResponseEntity.status(404).body("Reservation not found.");
            }

            // Get by id
            Reservation reservation = reservationRepository.findById(id).get();

            // JWT check
            User authenticated = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            if (jwtUtil.hasRole("ROLE_REGULAR") && authenticated.getId() != reservation.getUser().getId()) {
                return ResponseEntity.status(401).body("You cannot see another users' reservation.");
            }

            return ResponseEntity.ok(new ReservationResponseDto(reservation));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    public ResponseEntity<?> deleteReservationById(int id) {
        try {
            // Id check
            if (!reservationRepository.existsById(id)) {
                return ResponseEntity.status(404).body("Reservation with provided id not found.");
            }

            // Get by id
            Reservation reservation = reservationRepository.findById(id).get();

            // JWT check
            User authenticated = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            if (jwtUtil.hasRole("ROLE_REGULAR") && authenticated.getId() != reservation.getUser().getId()) {
                return ResponseEntity.status(401).body("You cannot delete another user's reservation.");
            }

            // Delete
            Ticket departureTicket = reservation.getDepartureTicket();
            Ticket returnTicket = reservation.getReturnTicket();
            departureTicket.setUser(null);
            ticketRepository.save(departureTicket);
            if (returnTicket != null) {
                returnTicket.setUser(null);
                ticketRepository.save(returnTicket);
            }
            reservationRepository.deleteById(id);

            return ResponseEntity.ok(new JsonMessage("Reservation deleted."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }
}
