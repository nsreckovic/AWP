package com.ns.awp.airport.controller;

import com.ns.awp.airport.models.Airport;
import com.ns.awp.airport.service.AirportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/airports")
@RequiredArgsConstructor
public class AirportController {
    private final AirportService airportService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> newAirport(@RequestBody Airport airport) {
        return airportService.newAirport(airport);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ResponseEntity<?> updateAirport(@RequestBody Airport airport) {
        return airportService.updateAirport(airport);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'REGULAR')")
    @GetMapping("/all")
    public ResponseEntity<?> getAllAirports() {
        return airportService.getAllAirports();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'REGULAR')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getAirportById(@PathVariable("id") Integer id) {
        return airportService.getAirportByAirportId(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAirportById(@PathVariable("id") Integer id) {
        return airportService.deleteAirportByAirportId(id);
    }
}
