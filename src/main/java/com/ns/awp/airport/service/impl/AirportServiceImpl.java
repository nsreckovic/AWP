package com.ns.awp.airport.service.impl;

import com.ns.awp.airport.models.Airport;
import com.ns.awp.airport.repository.AirportRepository;
import com.ns.awp.airport.service.AirportService;
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
                return ResponseEntity.status(400).body("Airport with provided new airport id already exists.");
            } else {
                existing.setAirportId(airport.getAirportId());
            }

            // Name check
            if (!existing.getName().equals(airport.getName()) && airportRepository.existsByName(airport.getName())) {
                return ResponseEntity.status(400).body("Airport with provided new name already exists.");
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
    public ResponseEntity<?> getAirportByAirportId(String id) {
        try {
            // Get by airport id
            Airport airport = airportRepository.findByAirportId(id).get();

            return ResponseEntity.ok(airport);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Airport with provided airport id not found.");
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteAirportByAirportId(String id) {
        try {
            // Airport id check
            if (!airportRepository.existsByAirportId(id)) {
                return ResponseEntity.status(404).body("Airport with provided airport id not found.");
            }

            // Delete
            airportRepository.deleteByAirportId(id);

            return ResponseEntity.ok("Airport deleted.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }
}
