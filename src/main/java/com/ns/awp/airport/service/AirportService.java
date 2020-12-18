package com.ns.awp.airport.service;

import com.ns.awp.airport.models.Airport;
import org.springframework.http.ResponseEntity;

public interface AirportService {
    ResponseEntity<?> newAirport(Airport airport);

    ResponseEntity<?> updateAirport(Airport airport);

    ResponseEntity<?> getAllAirports();

    ResponseEntity<?> getAirportByAirportId(String id);

    ResponseEntity<?> deleteAirportByAirportId(String id);
}
