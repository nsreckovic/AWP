package com.ns.awp.flightInstance.service;

import com.ns.awp.flightInstance.models.dto.FlightInstanceSaveRequestDto;
import org.springframework.http.ResponseEntity;

public interface FlightInstanceService {
    ResponseEntity<?> newFlightInstance(FlightInstanceSaveRequestDto flightInstance);

    ResponseEntity<?> updateFlightInstance(FlightInstanceSaveRequestDto flightInstance);

    ResponseEntity<?> getAllFlightInstances();

    ResponseEntity<?> getFlightInstanceById(Integer id);

    ResponseEntity<?> deleteFlightInstanceById(Integer id);
}
