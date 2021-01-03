package com.ns.awp.flight.repository;

import com.ns.awp.flight.models.Flight;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface FlightRepository extends CrudRepository<Flight, Integer> {
    @Query("SELECT f FROM Flight f ORDER BY f.departureAirport.airportId ASC, f.arrivalAirport.airportId ASC, f.airline.name ASC")
    Iterable<Flight> findAllSorted();

    boolean existsByFlightId(String id);
}
