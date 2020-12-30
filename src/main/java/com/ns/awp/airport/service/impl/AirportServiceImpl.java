package com.ns.awp.airport.service.impl;

import com.ns.awp.airport.models.Airport;
import com.ns.awp.airport.repository.AirportRepository;
import com.ns.awp.airport.service.AirportService;
import com.ns.awp.config.JsonMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class AirportServiceImpl implements AirportService {
    private final AirportRepository airportRepository;

    @Override
    public ResponseEntity<?> newAirport(Airport airport) {
        try {
            // Null check
            if (airport.getAirportId() == null) {
                return ResponseEntity.status(400).body("Airport id cannot be null.");
            } else {
                airport.setAirportId(airport.getAirportId().toUpperCase());
            }
            if (airport.getName() == null) {
                return ResponseEntity.status(400).body("Airport name cannot be null.");
            }
            if (airport.getPlace() == null) {
                return ResponseEntity.status(400).body("Airport place cannot be null.");
            }

            // Airport id check
            if (airportRepository.existsByAirportId(airport.getAirportId())) {
                return ResponseEntity.status(400).body("Airport with provided airport id already exists.");
            }

            // Name check
            if (airportRepository.existsByName(airport.getName())) {
                return ResponseEntity.status(400).body("Airport with provided name already exists.");
            }

            // Save
            airport.setId(-1);
            airport = airportRepository.save(airport);

            return ResponseEntity.ok(airport);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    @Override
    public ResponseEntity<?> updateAirport(Airport airport) {
        try {
            // Id check
            if (!airportRepository.existsById(airport.getId())) {
                return ResponseEntity.status(404).body("Airport with provided id not found.");
            }
            Airport existing = airportRepository.findById(airport.getId()).get();

            // Airport id check
            if (!existing.getAirportId().equals(airport.getAirportId()) && airportRepository.existsByAirportId(airport.getAirportId())) {
                return ResponseEntity.status(400).body("Airport with provided airport id already exists.");
            } else {
                existing.setAirportId(airport.getAirportId());
            }

            // Name check
            if (!existing.getName().equals(airport.getName()) && airportRepository.existsByName(airport.getName())) {
                return ResponseEntity.status(400).body("Airport with provided name already exists.");
            } else {
                existing.setName(airport.getName());
            }

            // Place check
            if (airport.getPlace() != null) {
                existing.setPlace(airport.getPlace());
            }

            // Save
            existing = airportRepository.save(existing);

            return ResponseEntity.ok(existing);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    @Override
    public ResponseEntity<?> getAllAirports() {
        try {
            // Get all
            Iterable<Airport> airports = airportRepository.findAll();

            return ResponseEntity.ok(airports);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    @Override
    public ResponseEntity<?> getAirportByAirportId(Integer id) {
        try {
            // Airport id check
            if (!airportRepository.existsById(id)) {
                return ResponseEntity.status(404).body("Airport with provided id not found.");
            }

            // Get by airport id
            Airport airport = airportRepository.findById(id).get();

            return ResponseEntity.ok(airport);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    @Override
    public ResponseEntity<?> deleteAirportByAirportId(Integer id) {
        try {
            // Airport id check
            if (!airportRepository.existsById(id)) {
                return ResponseEntity.status(404).body("Airport with provided id not found.");
            }

            // Delete
            airportRepository.deleteById(id);

            return ResponseEntity.ok(new JsonMessage("Airport deleted."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }
}
