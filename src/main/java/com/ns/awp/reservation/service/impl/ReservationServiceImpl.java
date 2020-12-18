package com.ns.awp.reservation.service.impl;

import com.ns.awp.reservation.models.Reservation;
import com.ns.awp.reservation.models.dto.ReservationResponseDto;
import com.ns.awp.reservation.models.dto.ReservationSaveRequestDto;
import com.ns.awp.reservation.repository.ReservationRepository;
import com.ns.awp.reservation.service.ReservationService;
import com.ns.awp.ticket.models.Ticket;
import com.ns.awp.ticket.repository.TicketRepository;
import com.ns.awp.user.models.User;
import com.ns.awp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<?> newReservation(ReservationSaveRequestDto reservation) {
        try {
            // Null check
            if (reservation.getUserId() == null) {
                return ResponseEntity.status(400).body("User id cannot be null.");
            } else if (reservation.getDepartureTicketId() == null) {
                return ResponseEntity.status(400).body("Departure ticket id cannot be null.");
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
                    } else {
                        departureTicket.setUser(user);
                    }
                }
            }

            // Return ticket
            Ticket returnTicket;
            if (reservation.getReturnTicketId() != null && !ticketRepository.existsById(reservation.getReturnTicketId())) {
                return ResponseEntity.status(404).body("Ticket not found.");
            } else {
                returnTicket = reservation.getReturnTicketId() != null ? ticketRepository.findById(reservation.getReturnTicketId()).get() : null;
                if (returnTicket != null) {
                    if (returnTicket.getUser() != null) {
                        return ResponseEntity.status(400).body("Return ticket with provided id has already been assigned to the user.");
                    } else {
                        returnTicket.setUser(user);
                    }
                    returnTicket.setUser(user);
                }
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

    @Override
    public ResponseEntity<?> updateReservation(ReservationSaveRequestDto reservation) {
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

            // Reservation timestamp
            Timestamp currentTimestamp = new Timestamp(new Date().getTime());

            // Update time check
            if ((existing.getDepartureTicket().getFlightInstance().getFlightDate().getTime() + TimeUnit.DAYS.toMillis(1)) <= currentTimestamp.getTime()) {
                return ResponseEntity.status(400).body("Reservation cannot be changed the day before or after the departure flight.");
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
                            if (currentTimestamp.after(newDepartureTicket.getFlightInstance().getFlightDate())) {
                                return ResponseEntity.status(400).body("Reservation cannot be made for a flight in the past.");
                            } else {
                                // Reset old departure ticket
                                oldDepartureTicket.setUser(null);
                                // Set new departure ticket
                                newDepartureTicket.setUser(existing.getUser());
                                existing.setDepartureTicket(newDepartureTicket);
                            }
                        }
                    } else {
                        oldDepartureTicket = null;
                        newDepartureTicket = null;
                    }
                }
            } else if (oldUser != null && !oldUser.equals(existing.getUser())){
                existing.getDepartureTicket().setUser(existing.getUser());
                oldDepartureTicket = existing.getDepartureTicket();
            }

            // Return ticket
            Ticket oldReturnTicket = null;
            Ticket newReturnTicket = null;
            if (reservation.getReturnTicketId() != null && reservation.getReturnTicketId() == -1 && existing.getReturnTicket() != null) {
                // Reset old return ticket
                oldReturnTicket = existing.getReturnTicket();
                oldReturnTicket.setUser(null);
                // Set new return ticket
                existing.setReturnTicket(null);

            } else if (reservation.getReturnTicketId() != null && reservation.getReturnTicketId() != -1) {
                if (!ticketRepository.existsById(reservation.getReturnTicketId())) {
                    return ResponseEntity.status(404).body("Ticket not found.");
                } else {
                    oldReturnTicket = existing.getReturnTicket();
                    newReturnTicket = ticketRepository.findById(reservation.getReturnTicketId()).get();

                    if (oldReturnTicket == null || !oldReturnTicket.equals(newReturnTicket)){
                        if (newReturnTicket.getUser() != null) {
                            return ResponseEntity.status(400).body("Return ticket with provided id has already been assigned to the user.");
                        } else if (newReturnTicket.getFlightInstance().getFlightDate().before(existing.getDepartureTicket().getFlightInstance().getFlightDate())) {
                            return ResponseEntity.status(400).body("Return ticket must be after the departure ticket.");
                        } else if (!existing.getDepartureTicket().getFlightInstance().getFlight().getArrivalAirport().getId().equals(newReturnTicket.getFlightInstance().getFlight().getDepartureAirport().getId())) {
                            return ResponseEntity.status(400).body("Arrival airport of the departure ticket must be the same as the departure airport of the return ticket.");
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

    @Override
    public ResponseEntity<?> getAllReservations() {
        try {
            // Get all
            Iterable<Reservation> reservations = reservationRepository.findAll();

            return ResponseEntity.ok(reservations);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    @Override
    public ResponseEntity<?> getReservationById(int id) {
        try {
            // Id check
            if (!reservationRepository.existsById(id)) {
                return ResponseEntity.status(404).body("Reservation not found.");
            }

            // Get by id
            Reservation reservation = reservationRepository.findById(id).get();

            return ResponseEntity.ok(reservation);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    @Override
    public ResponseEntity<?> deleteReservationById(int id) {
        try {
            // Id check
            if (!reservationRepository.existsById(id)) {
                return ResponseEntity.status(404).body("Reservation not found.");
            }

            // Delete
            reservationRepository.deleteById(id);

            return ResponseEntity.ok("Reservation deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }
}
