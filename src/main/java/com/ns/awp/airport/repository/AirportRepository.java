package com.ns.awp.airport.repository;

import com.ns.awp.airport.models.Airport;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AirportRepository extends CrudRepository<Airport, Integer> {
    boolean existsByName(String name);
    boolean existsByAirportId(String id);
}
