package com.ns.awp.flightInstance.service.impl;

import com.ns.awp.flight.models.Flight;
import com.ns.awp.flight.repository.FlightRepository;
import com.ns.awp.flightInstance.models.FlightInstance;
import com.ns.awp.flightInstance.models.dto.FlightInstanceResponseDto;
import com.ns.awp.flightInstance.models.dto.FlightInstanceRequestDto;
import com.ns.awp.flightInstance.repository.FlightInstanceRepository;
import com.ns.awp.flightInstance.service.FlightInstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightInstanceServiceImpl implements FlightInstanceService {
    private final FlightInstanceRepository flightInstanceRepository;
    private final FlightRepository flightRepository;

    @Override
    public ResponseEntity<?> newFlightInstance(FlightInstanceRequestDto flightInstance) {
        try {
            // Null check
            if (flightInstance.getFlightId() == null) {
                return ResponseEntity.status(400).body("Flight id cannot be null.");
            }
            if (flightInstance.getFlightDate() == null) {
                return ResponseEntity.status(400).body("Flight date cannot be null.");
            }
            if (flightInstance.getFlightLengthInMinutes() == null) {
                return ResponseEntity.status(400).body("Flight length cannot be null.");
            }
            if (flightInstance.getCount() == null || flightInstance.getCount() < 1) {
                return ResponseEntity.status(400).body("Count cannot be null or less than 1.");
            }

            // Flight length check
            if (flightInstance.getFlightLengthInMinutes() <= 30) {
                return ResponseEntity.status(400).body("Flight cannot be shorter than half an hour.");
            }
            if (flightInstance.getFlightLengthInMinutes() >= 1200) {
                return ResponseEntity.status(400).body("Flight cannot be longer than 20 hours.");
            }

            // Flight check
            Flight flight;
            if (!flightRepository.existsByFlightId(flightInstance.getFlightId())) {
                return ResponseEntity.status(404).body("Flight with provided id not found.");
            } else {
                flight = flightRepository.findByFlightId(flightInstance.getFlightId()).get();
            }

            // Count check
            if (flightInstance.getCount() < 1) {
                return ResponseEntity.status(400).body("Count must be greater than 0.");
            }

            Timestamp flightDate = new Timestamp(flightInstance.getFlightDate());

            // Flight instance check
            if (flightInstanceRepository.existsByFlightAndFlightDate(flight, flightDate)) {
                return ResponseEntity.status(400).body("Flight instance already exists.");
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

    @Override
    public ResponseEntity<?> updateFlightInstance(FlightInstanceRequestDto flightInstance) {
        try {
            // Id check
            FlightInstance existing;
            if (flightInstance.getId() != null && flightInstanceRepository.existsById(flightInstance.getId())) {
                existing = flightInstanceRepository.findById(flightInstance.getId()).get();
            } else {
                return ResponseEntity.status(404).body("Flight instance not found.");
            }

            // New flight check
            if (flightInstance.getFlightId() != null) {
                if (!flightRepository.existsByFlightId(flightInstance.getFlightId())) {
                    return ResponseEntity.status(404).body("Flight with provided id not found.");
                } else {
                    existing.setFlight(flightRepository.findByFlightId(flightInstance.getFlightId()).get());
                }
            }

            // New date check
            if (flightInstance.getFlightDate() != null) {
                existing.setFlightDate(new Timestamp(flightInstance.getFlightDate()));
            }

            // Flight length check
            if (flightInstance.getFlightLengthInMinutes() != null) {
                if (flightInstance.getFlightLengthInMinutes() <= 30) {
                    return ResponseEntity.status(400).body("Flight cannot be shorter than half an hour.");
                } else if (flightInstance.getFlightLengthInMinutes() >= 1200) {
                    return ResponseEntity.status(400).body("Flight cannot be longer than 20 hours.");
                } else {
                    existing.setFlightLengthInMinutes(flightInstance.getFlightLengthInMinutes());
                }
            }

            // Flight instance check
            if (flightInstanceRepository.existsByFlightAndFlightDateAndIdIsNot(existing.getFlight(), existing.getFlightDate(), existing.getId())) {
                return ResponseEntity.status(400).body("Flight instance already exists.");
            }

            // Count check
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

    @Override
    public ResponseEntity<?> getAllFlightInstances() {
        try {
            // Get all
            List<FlightInstanceResponseDto> flightInstances = new ArrayList<>();
            flightInstanceRepository.findAll().forEach(flightInstance -> flightInstances.add(new FlightInstanceResponseDto(flightInstance)));

            return ResponseEntity.ok(flightInstances);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    @Override
    public ResponseEntity<?> getFlightInstanceById(Integer id) {
        try {
            // Id check
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

    @Override
    @Transactional
    public ResponseEntity<?> deleteFlightInstanceById(Integer id) {
        try {
            // Id check
            if (!flightInstanceRepository.existsById(id)) {
                return ResponseEntity.status(404).body("Flight instance not found.");
            }

            // Delete
            flightInstanceRepository.deleteById(id);

            return ResponseEntity.ok("Flight Instance deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }
}
