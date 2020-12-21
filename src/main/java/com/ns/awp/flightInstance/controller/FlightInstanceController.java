package com.ns.awp.flightInstance.controller;

import com.ns.awp.flightInstance.models.dto.FlightInstanceSaveRequestDto;
import com.ns.awp.flightInstance.service.FlightInstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/flightInstances")
@RequiredArgsConstructor
public class FlightInstanceController {
    private final FlightInstanceService flightInstanceService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<?> newFlightInstance(@RequestBody FlightInstanceSaveRequestDto flightInstance) {
        return flightInstanceService.newFlightInstance(flightInstance);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/")
    public ResponseEntity<?> updateFlightInstance(@RequestBody FlightInstanceSaveRequestDto flightInstance) {
        return flightInstanceService.updateFlightInstance(flightInstance);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'REGULAR')")
    @GetMapping("/all")
    public ResponseEntity<?> getAllFlightInstances() {
        return flightInstanceService.getAllFlightInstances();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'REGULAR')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getFlightInstanceById(@PathVariable("id") int id) {
        return flightInstanceService.getFlightInstanceById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFlightInstanceById(@PathVariable("id") int id) {
        return flightInstanceService.deleteFlightInstanceById(id);
    }
}
