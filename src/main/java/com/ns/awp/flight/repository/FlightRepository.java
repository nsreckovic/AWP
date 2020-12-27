package com.ns.awp.flight.repository;

import com.ns.awp.flight.models.Flight;
import org.springframework.data.repository.CrudRepository;

public interface FlightRepository extends CrudRepository<Flight, Integer> {
    boolean existsByFlightId(String id);
}
