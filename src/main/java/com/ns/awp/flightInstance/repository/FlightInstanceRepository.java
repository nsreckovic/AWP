package com.ns.awp.flightInstance.repository;

import com.ns.awp.flight.models.Flight;
import com.ns.awp.flightInstance.models.FlightInstance;
import org.springframework.data.repository.CrudRepository;

import java.sql.Timestamp;

public interface FlightInstanceRepository extends CrudRepository<FlightInstance, Integer> {
    Iterable<FlightInstance> findAllByFlight(Flight flight);

    boolean existsByFlightAndFlightDate(Flight flight, Timestamp flightDate);

    boolean existsByFlightAndFlightDateAndIdIsNot(Flight flight, Timestamp flightDate, int existingId);
}
