package com.ns.awp.flight.controller;

import com.ns.awp.flight.models.dto.FlightRequestDto;
import com.ns.awp.flight.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/flights")
@RequiredArgsConstructor
public class FlightController {
    private final FlightService flightService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> newFlight(@RequestBody FlightRequestDto flight) {
        return flightService.newFlight(flight);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ResponseEntity<?> updateFlight(@RequestBody FlightRequestDto flight) {
        return flightService.updateFlight(flight);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'REGULAR')")
    @GetMapping("/all")
    public ResponseEntity<?> getAllFlights() {
        return flightService.getAllFlights();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'REGULAR')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getFlightById(@PathVariable("id") Integer id) {
        return flightService.getFlightByFlightId(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFlightById(@PathVariable("id") Integer id) {
        return flightService.deleteFlightByFlightId(id);
    }
}
