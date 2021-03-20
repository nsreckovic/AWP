package com.ns.awp.flightInstance.service;

import com.ns.awp.config.security.JsonMessage;
import com.ns.awp.flight.models.Flight;
import com.ns.awp.flight.repository.FlightRepository;
import com.ns.awp.flightInstance.models.FlightInstance;
import com.ns.awp.flightInstance.models.dto.FlightInstanceResponseDto;
import com.ns.awp.flightInstance.models.dto.FlightInstanceRequestDto;
import com.ns.awp.flightInstance.repository.FlightInstanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightInstanceService {
    private final FlightInstanceRepository flightInstanceRepository;
    private final FlightRepository flightRepository;

    public ResponseEntity<?> newFlightInstance(FlightInstanceRequestDto flightInstance) {
        try {
            // Flight
            if (flightInstance.getFlightId() == null) {
                return ResponseEntity.status(400).body("Flight id cannot be null.");
            }
            Flight flight;
            if (!flightRepository.existsById(flightInstance.getFlightId())) {
                return ResponseEntity.status(404).body("Flight with provided id not found.");
            } else {
                flight = flightRepository.findById(flightInstance.getFlightId()).get();
            }

            // Flight date
            if (flightInstance.getFlightDate() == null) {
                return ResponseEntity.status(400).body("Flight date cannot be null.");
            }
            Timestamp flightDate = new Timestamp(flightInstance.getFlightDate());

            // Existing flight instance
            if (flightInstanceRepository.existsByFlightAndFlightDate(flight, flightDate)) {
                return ResponseEntity.status(400).body("Flight instance already exists.");
            }

            // Flight length in minutes
            if (flightInstance.getFlightLengthInMinutes() == null) {
                return ResponseEntity.status(400).body("Flight length cannot be null.");
            }
            if (flightInstance.getFlightLengthInMinutes() <= 30) {
                return ResponseEntity.status(400).body("Flight cannot be shorter than half an hour.");
            } else if (flightInstance.getFlightLengthInMinutes() >= 1200) {
                return ResponseEntity.status(400).body("Flight cannot be longer than 20 hours (1200 minutes).");
            }

            // Count
            if (flightInstance.getCount() == null || flightInstance.getCount() < 1) {
                return ResponseEntity.status(400).body("Count cannot be null or less than 1.");
            }

            // Save
            FlightInstance saved = flightInstanceRepository.save(new FlightInstance(
                    -1,
                    flight,
                    flightDate,
                    flightInstance.getFlightLengthInMinutes(),
                    flightInstance.getCount(),
                    null
            ));

            return ResponseEntity.ok(new FlightInstanceResponseDto(saved));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    public ResponseEntity<?> updateFlightInstance(FlightInstanceRequestDto flightInstance) {
        try {
            // Validation
            FlightInstance existing;
            if (flightInstance.getId() != null && flightInstanceRepository.existsById(flightInstance.getId())) {
                existing = flightInstanceRepository.findById(flightInstance.getId()).get();
            } else {
                return ResponseEntity.status(404).body("Flight instance with provided id not found.");
            }

            // Flight
            if (flightInstance.getFlightId() != null && flightInstance.getFlightId() != existing.getFlight().getId()) {
                if (!flightRepository.existsById(flightInstance.getFlightId())) {
                    return ResponseEntity.status(404).body("Flight with provided id not found.");
                } else {
                    existing.setFlight(flightRepository.findById(flightInstance.getFlightId()).get());
                }
            }

            // Date
            if (flightInstance.getFlightDate() != null) {
                existing.setFlightDate(new Timestamp(flightInstance.getFlightDate()));
            }

            // Flight length
            if (flightInstance.getFlightLengthInMinutes() != null) {
                if (flightInstance.getFlightLengthInMinutes() <= 30) {
                    return ResponseEntity.status(400).body("Flight cannot be shorter than half an hour.");
                } else if (flightInstance.getFlightLengthInMinutes() >= 1200) {
                    return ResponseEntity.status(400).body("Flight cannot be longer than 20 hours.");
                } else {
                    existing.setFlightLengthInMinutes(flightInstance.getFlightLengthInMinutes());
                }
            }

            // Flight instance
            if (flightInstanceRepository.existsByFlightAndFlightDateAndIdIsNot(existing.getFlight(), existing.getFlightDate(), existing.getId())) {
                return ResponseEntity.status(400).body("Flight instance already exists.");
            }

            // Count
            if (flightInstance.getCount() != null) {
                if (flightInstance.getCount() < 1) {
                    return ResponseEntity.status(400).body("Count must be greater than 0.");
                } else {
                    existing.setCount(flightInstance.getCount());
                }
            }

            // Save
            existing = flightInstanceRepository.save(existing);

            return ResponseEntity.ok(new FlightInstanceResponseDto(existing));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    public ResponseEntity<?> getAllFlightInstances() {
        try {
            // Get all
            List<FlightInstanceResponseDto> flightInstances = new ArrayList<>();
            flightInstanceRepository.findAllSorted().forEach(flightInstance -> flightInstances.add(new FlightInstanceResponseDto(flightInstance)));

            return ResponseEntity.ok(flightInstances);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    public ResponseEntity<?> getFlightInstanceById(Integer id) {
        try {
            // Validation
            if (!flightInstanceRepository.existsById(id)) {
                return ResponseEntity.status(404).body("Flight instance not found.");
            }

            // Get by id
            FlightInstance flightInstance = flightInstanceRepository.findById(id).get();

            return ResponseEntity.ok(new FlightInstanceResponseDto(flightInstance));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    public ResponseEntity<?> deleteFlightInstanceById(Integer id) {
        try {
            // Validation
            if (!flightInstanceRepository.existsById(id)) {
                return ResponseEntity.status(404).body("Flight instance not found.");
            }

            // Delete
            flightInstanceRepository.deleteById(id);

            return ResponseEntity.ok(new JsonMessage("Flight Instance deleted."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }
}
