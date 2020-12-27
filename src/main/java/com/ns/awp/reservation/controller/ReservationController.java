package com.ns.awp.reservation.controller;

import com.ns.awp.reservation.models.dto.ReservationFilter;
import com.ns.awp.reservation.models.dto.ReservationRequestDto;
import com.ns.awp.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PreAuthorize("hasAnyRole('ADMIN', 'REGULAR')")
    @PostMapping
    public ResponseEntity<?> newReservation(@RequestBody ReservationRequestDto reservation) {
        return reservationService.newReservation(reservation);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'REGULAR')")
    @PutMapping
    public ResponseEntity<?> updateReservation(@RequestBody ReservationRequestDto reservation) {
        return reservationService.updateReservation(reservation);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'REGULAR')")
    @PostMapping("/all")
    public ResponseEntity<?> getAllReservations(@RequestBody ReservationFilter filter) {
        return reservationService.getAllReservations(filter);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'REGULAR')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getReservationById(@PathVariable("id") Integer id) {
        return reservationService.getReservationById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'REGULAR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReservationById(@PathVariable("id") Integer id) {
        return reservationService.deleteReservationById(id);
    }
    
}
