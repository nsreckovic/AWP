package com.ns.awp.flightInstance.repository;

import com.ns.awp.flight.models.Flight;
import com.ns.awp.flightInstance.models.FlightInstance;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.sql.Timestamp;

public interface FlightInstanceRepository extends CrudRepository<FlightInstance, Integer> {
    @Query("SELECT fi FROM FlightInstance fi ORDER BY fi.flightDate ASC, fi.flight.departureAirport.airportId ASC, fi.flight.arrivalAirport.airportId ASC, fi.flight.airline.name ASC, fi.flightLengthInMinutes ASC")
    Iterable<FlightInstance> findAllSorted();

    boolean existsByFlightAndFlightDate(Flight flight, Timestamp flightDate);

    boolean existsByFlightAndFlightDateAndIdIsNot(Flight flight, Timestamp flightDate, int existingId);
}
