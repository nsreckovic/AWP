package com.ns.awp.flight.service;

import com.ns.awp.flight.models.dto.FlightRequestDto;
import org.springframework.http.ResponseEntity;

public interface FlightService {
    ResponseEntity<?> newFlight(FlightRequestDto flight);

    ResponseEntity<?> updateFlight(FlightRequestDto flight);

    ResponseEntity<?> getAllFlights();

    ResponseEntity<?> getFlightByFlightId(Integer id);

    ResponseEntity<?> deleteFlightByFlightId(Integer id);
}
