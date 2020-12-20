package com.ns.awp.reservation.service;

import com.ns.awp.reservation.models.dto.ReservationFilter;
import com.ns.awp.reservation.models.dto.ReservationSaveRequestDto;
import org.springframework.http.ResponseEntity;

public interface ReservationService {
    ResponseEntity<?> newReservation(ReservationSaveRequestDto reservation);

    ResponseEntity<?> updateReservation(ReservationSaveRequestDto reservation);

    ResponseEntity<?> getAllReservations(ReservationFilter filter);

    ResponseEntity<?> getReservationById(int id);

    ResponseEntity<?> deleteReservationById(int id);
}
