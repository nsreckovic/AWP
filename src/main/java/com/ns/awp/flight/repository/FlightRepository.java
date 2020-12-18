package com.ns.awp.flight.repository;

import com.ns.awp.flight.models.Flight;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FlightRepository extends CrudRepository<Flight, Integer> {
    boolean existsByFlightId(String id);

    Optional<Flight> findByFlightId(String id);

    void deleteByFlightId(String id);
}
