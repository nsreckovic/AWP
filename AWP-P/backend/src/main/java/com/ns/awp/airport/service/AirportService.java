package com.ns.awp.airport.service;

import com.ns.awp.airport.models.Airport;
import com.ns.awp.airport.repository.AirportRepository;
import com.ns.awp.config.security.JsonMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AirportService {
    private final AirportRepository airportRepository;
    private final String alphanumericRegex = "^[a-zA-Z0-9]+$";
    private final String alphanumericRegexWithSpace = "^[a-zA-Z0-9 ]+$";

    public ResponseEntity<?> newAirport(Airport airport) {
        try {
            // Airport Id
            if (airport.getAirportId() == null || airport.getAirportId().isBlank()) {
                return ResponseEntity.status(400).body("Airport id cannot be null or empty.");
            } else if (!airport.getAirportId().matches(alphanumericRegex)) {
                return ResponseEntity.status(400).body("Airport id must consist of alphanumeric characters only.");
            } else {
                airport.setAirportId(airport.getAirportId().toUpperCase());
                if (airportRepository.existsByAirportId(airport.getAirportId())) {
                    return ResponseEntity.status(400).body("Airport with provided airport id already exists.");
                }
            }

            // Name
            if (airport.getName() == null || airport.getName().isBlank()) {
                return ResponseEntity.status(400).body("Airport name cannot be null or empty.");
            } else if (!airport.getName().matches(alphanumericRegexWithSpace)) {
                return ResponseEntity.status(400).body("Airport name must consist of alphanumeric characters only.");
            } else if (airportRepository.existsByName(airport.getName())) {
                return ResponseEntity.status(400).body("Airport with provided name already exists.");
            }

            // Place
            if (airport.getPlace() == null || airport.getPlace().isBlank()) {
                return ResponseEntity.status(400).body("Airport place cannot be null or empty.");
            } else if (!airport.getPlace().matches(alphanumericRegexWithSpace)) {
                return ResponseEntity.status(400).body("Airport place must consist of alphanumeric characters only.");
            }

            // Save
            airport.setId(-1);
            airport = airportRepository.save(airport);

            return ResponseEntity.ok(airport);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    public ResponseEntity<?> updateAirport(Airport airport) {
        try {
            // Validation
            Airport existing;
            if (airport.getId() != null && airportRepository.existsById(airport.getId())) {
                existing = airportRepository.findById(airport.getId()).get();
            } else {
                return ResponseEntity.status(404).body("Airport with provided id not found.");
            }

            // Airport Id
            if (airport.getAirportId() != null && !airport.getAirportId().isBlank()) {
                if (!airport.getAirportId().matches(alphanumericRegex)) {
                    return ResponseEntity.status(400).body("Airport id must consist of alphanumeric characters only.");
                }
                if (!existing.getAirportId().equals(airport.getAirportId()) && airportRepository.existsByAirportId(airport.getAirportId())) {
                    return ResponseEntity.status(400).body("Airport with provided airport id already exists.");
                } else {
                    existing.setAirportId(airport.getAirportId());
                }
            }

            // Name
            if (airport.getName() != null && !airport.getName().isBlank()) {
                if (!airport.getName().matches(alphanumericRegexWithSpace)) {
                    return ResponseEntity.status(400).body("Airport name must consist of alphanumeric characters only.");
                }
                if (!existing.getName().equals(airport.getName()) && airportRepository.existsByName(airport.getName())) {
                    return ResponseEntity.status(400).body("Airport with provided name already exists.");
                } else {
                    existing.setName(airport.getName());
                }
            }

            // Place
            if (airport.getPlace() != null && !airport.getPlace().isBlank()) {
                if (!airport.getPlace().matches(alphanumericRegexWithSpace)) {
                    return ResponseEntity.status(400).body("Airport place must consist of alphanumeric characters only.");
                }
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

    public ResponseEntity<?> getAllAirports() {
        try {
            // Get all
            Iterable<Airport> airports = airportRepository.findAllSorted();

            return ResponseEntity.ok(airports);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    public ResponseEntity<?> getAirportByAirportId(Integer id) {
        try {
            // Validation
            if (!airportRepository.existsById(id)) {
                return ResponseEntity.status(404).body("Airport with provided id not found.");
            }

            // Get by id
            Airport airport = airportRepository.findById(id).get();

            return ResponseEntity.ok(airport);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    public ResponseEntity<?> deleteAirportByAirportId(Integer id) {
        try {
            // Validation
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
