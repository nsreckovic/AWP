package com.ns.awp.flightInstance.service;

import com.ns.awp.flightInstance.models.dto.FlightInstanceRequestDto;
import org.springframework.http.ResponseEntity;

public interface FlightInstanceService {
    ResponseEntity<?> newFlightInstance(FlightInstanceRequestDto flightInstance);

    ResponseEntity<?> updateFlightInstance(FlightInstanceRequestDto flightInstance);

    ResponseEntity<?> getAllFlightInstances();

    ResponseEntity<?> getFlightInstanceById(Integer id);

    ResponseEntity<?> deleteFlightInstanceById(Integer id);
}
