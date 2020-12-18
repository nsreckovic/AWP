package com.ns.awp.reservation.controller;

import com.ns.awp.reservation.models.dto.ReservationSaveRequestDto;
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

    //@PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<?> newReservation(@RequestBody ReservationSaveRequestDto reservation) {
        return reservationService.newReservation(reservation);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/")
    public ResponseEntity<?> updateReservation(@RequestBody ReservationSaveRequestDto reservation) {
        return reservationService.updateReservation(reservation);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReservationById(@PathVariable("id") Integer id) {
        return reservationService.getReservationById(id);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReservationById(@PathVariable("id") Integer id) {
        return reservationService.deleteReservationById(id);
    }
    
}
