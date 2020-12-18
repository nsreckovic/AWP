package com.ns.awp.airline.service;

import com.ns.awp.airline.models.Airline;
import org.springframework.http.ResponseEntity;

public interface AirlineService {
    ResponseEntity<?> newAirline(Airline airline);

    ResponseEntity<?> updateAirline(Airline airline);

    ResponseEntity<?> getAllAirlines();

    ResponseEntity<?> getAirlineById(int id);

    ResponseEntity<?> deleteAirlineById(int id);
}
