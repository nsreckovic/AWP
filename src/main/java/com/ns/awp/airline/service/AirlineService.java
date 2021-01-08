package com.ns.awp.airline.service;

import com.ns.awp.airline.models.Airline;
import com.ns.awp.airline.repository.AirlineRepository;
import com.ns.awp.config.security.JsonMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AirlineService {
    private final AirlineRepository airlineRepository;
    private final String alphanumericRegex = "^[a-zA-Z0-9 ]+$";
    private final String linkRegex = "^(https?:\\/\\/)?((([a-z\\d]([a-z\\d-]*[a-z\\d])*)\\.)+[a-z]{2,}|((\\d{1,3}\\.){3}\\d{1,3}))(\\:\\d+)?(\\/[-a-z\\d%_.~+]*)*(\\?[;&a-z\\d%_.~+=-]*)?(\\#[-a-z\\d_]*)?$";

    public ResponseEntity<?> newAirline(Airline airline) {
        try {
            // Name
            if (airline.getName() == null || airline.getName().isBlank()) {
                return ResponseEntity.status(400).body("Airline name cannot be null or empty.");
            } else if (!airline.getName().matches(alphanumericRegex)) {
                return ResponseEntity.status(400).body("Airline name must consist of alphanumeric characters only.");
            } else if (airlineRepository.existsByName(airline.getName())) {
                return ResponseEntity.status(400).body("Airline with provided name already exists.");
            }

            // Link
            if (airline.getLink() == null || airline.getLink().isBlank()) {
                return ResponseEntity.status(400).body("Airline link cannot be null or empty.");
            } else if (!airline.getLink().matches(linkRegex)) {
                return ResponseEntity.status(400).body("Airline link must be valid.");
            }

            // Save
            airline.setId(-1);
            airline.setFlights(null);
            airline = airlineRepository.save(airline);

            return ResponseEntity.ok(airline);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    public ResponseEntity<?> updateAirline(Airline airline) {
        try {
            // Validation
            Airline existing;
            if (airline.getId() != null && airlineRepository.existsById(airline.getId())) {
                existing = airlineRepository.findById(airline.getId()).get();
            } else {
                return ResponseEntity.status(404).body("Airline with provided id not found.");
            }

            // Name
            if (airline.getName() != null && !airline.getName().isBlank()) {
                if (!airline.getName().matches(alphanumericRegex)) {
                    return ResponseEntity.status(400).body("Airline name must consist of alphanumeric characters only.");
                }
                if (!existing.getName().equals(airline.getName()) && airlineRepository.existsByName(airline.getName())) {
                    return ResponseEntity.status(400).body("Airline with provided new name already exists.");
                } else {
                    existing.setName(airline.getName());
                }
            }

            // Link
            if (airline.getLink() != null && !airline.getLink().isBlank()) {
                if (!airline.getLink().matches(linkRegex)) {
                    return ResponseEntity.status(400).body("Airline link must be valid.");
                }
                existing.setLink(airline.getLink());
            }

            // Save
            existing = airlineRepository.save(existing);

            return ResponseEntity.ok(existing);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    public ResponseEntity<?> getAllAirlines() {
        try {
            // Get all
            Iterable<Airline> airlines = airlineRepository.findAllSorted();

            return ResponseEntity.ok(airlines);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    public ResponseEntity<?> getAirlineById(int id) {
        try {
            // Validation
            if (!airlineRepository.existsById(id)) {
                return ResponseEntity.status(404).body("Airline with provided id not found.");
            }

            // Get by id
            Airline airline = airlineRepository.findById(id).get();

            return ResponseEntity.ok(airline);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    public ResponseEntity<?> deleteAirlineById(int id) {
        try {
            // Validation
            if (!airlineRepository.existsById(id)) {
                return ResponseEntity.status(404).body("Airline with provided id not found.");
            }

            // Delete
            airlineRepository.deleteById(id);

            return ResponseEntity.ok(new JsonMessage("Airline deleted."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }
}
