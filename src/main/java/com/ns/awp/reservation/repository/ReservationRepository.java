package com.ns.awp.reservation.repository;

import com.ns.awp.reservation.models.Reservation;
import org.springframework.data.repository.CrudRepository;

public interface ReservationRepository extends CrudRepository<Reservation, Integer> {

}
