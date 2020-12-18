package com.ns.awp.flight.service;

import com.ns.awp.flight.models.dto.FlightSaveRequestDto;
import org.springframework.http.ResponseEntity;

public interface FlightService {
    ResponseEntity<?> newFlight(FlightSaveRequestDto flight);

    ResponseEntity<?> updateFlight(FlightSaveRequestDto flight);

    ResponseEntity<?> getAllFlights();

    ResponseEntity<?> getFlightByFlightId(String id);

    ResponseEntity<?> deleteFlightByFlightId(String id);
}
