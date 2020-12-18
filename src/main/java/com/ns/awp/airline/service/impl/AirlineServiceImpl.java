package com.ns.awp.airline.service.impl;

import com.ns.awp.airline.models.Airline;
import com.ns.awp.airline.repository.AirlineRepository;
import com.ns.awp.airline.service.AirlineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AirlineServiceImpl implements AirlineService {
    private final AirlineRepository airlineRepository;

    @Override
    public ResponseEntity<?> newAirline(Airline airline) {
        try {
            // Name check
            if (airlineRepository.existsByName(airline.getName())) {
                return ResponseEntity.status(400).body("Airline with provided name already exists.");
            }

            // Save
            airline.setId(-1);
            airline.setFlights(null);
            airline = airlineRepository.save(airline);

            return ResponseEntity.ok(airline);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    @Override
    public ResponseEntity<?> updateAirline(Airline airline) {
        try {
            // Id check
            if (!airlineRepository.existsById(airline.getId())) {
                return ResponseEntity.status(404).body("Airline with provided id not found.");
            }
            Airline existing = airlineRepository.findById(airline.getId()).get();

            // Name check
            if (!existing.getName().equals(airline.getName()) && airlineRepository.existsByName(airline.getName())) {
                return ResponseEntity.status(400).body("Airline with provided new name already exists.");
            } else {
                existing.setName(airline.getName());
            }

            // Set new link
            if (airline.getLink() != null) {
                existing.setLink(airline.getLink());
            }

            // Save
            existing = airlineRepository.save(existing);

            return ResponseEntity.ok(existing);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    @Override
    public ResponseEntity<?> getAllAirlines() {
        try {
            // Get all
            Iterable<Airline> airlines = airlineRepository.findAll();

            return ResponseEntity.ok(airlines);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    @Override
    public ResponseEntity<?> getAirlineById(int id) {
        try {
            // Get by id
            Airline airline = airlineRepository.findById(id).get();

            return ResponseEntity.ok(airline);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Airline with provided id not found.");
        }
    }

    @Override
    public ResponseEntity<?> deleteAirlineById(int id) {
        try {
            // Id check
            if (!airlineRepository.existsById(id)) {
                return ResponseEntity.status(404).body("Airline with provided id not found.");
            }

            // Delete
            airlineRepository.deleteById(id);

            return ResponseEntity.ok("Airline deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }
}
