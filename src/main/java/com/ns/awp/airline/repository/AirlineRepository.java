package com.ns.awp.airline.repository;

import com.ns.awp.airline.models.Airline;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AirlineRepository extends CrudRepository<Airline, Integer> {
    @Query("SELECT a FROM Airline a ORDER BY a.name ASC")
    Iterable<Airline> findAllSorted();

    boolean existsByName(String name);
}
