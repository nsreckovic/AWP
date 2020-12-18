package com.ns.awp.airline.repository;

import com.ns.awp.airline.models.Airline;
import org.springframework.data.repository.CrudRepository;

public interface AirlineRepository extends CrudRepository<Airline, Integer> {
    boolean existsByName(String name);
}
