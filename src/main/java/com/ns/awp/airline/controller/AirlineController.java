package com.ns.awp.airline.controller;

import com.ns.awp.airline.models.Airline;
import com.ns.awp.airline.service.AirlineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/airlines")
@RequiredArgsConstructor
public class AirlineController {
    private final AirlineService airlineService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<?> newAirline(@RequestBody Airline airline) {
        return airlineService.newAirline(airline);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/")
    public ResponseEntity<?> updateAirline(@RequestBody Airline airline) {
        return airlineService.updateAirline(airline);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'REGULAR')")
    @GetMapping("/all")
    public ResponseEntity<?> getAllAirlines() {
        return airlineService.getAllAirlines();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'REGULAR')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getAirlineById(@PathVariable("id") int id) {
        return airlineService.getAirlineById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAirlineById(@PathVariable("id") int id) {
        return airlineService.deleteAirlineById(id);
    }
}
