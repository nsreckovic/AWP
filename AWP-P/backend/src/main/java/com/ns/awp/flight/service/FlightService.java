package com.ns.awp.flight.service;

import com.ns.awp.airline.models.Airline;
import com.ns.awp.airline.repository.AirlineRepository;
import com.ns.awp.airport.models.Airport;
import com.ns.awp.airport.repository.AirportRepository;
import com.ns.awp.config.security.JsonMessage;
import com.ns.awp.flight.models.Flight;
import com.ns.awp.flight.models.dto.FlightRequestDto;
import com.ns.awp.flight.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FlightService {
    private final FlightRepository flightRepository;
    private final AirportRepository airportRepository;
    private final AirlineRepository airlineRepository;
    private final String alphanumericRegexWithSpace = "^[a-zA-Z0-9 ]+$";

    public ResponseEntity<?> newFlight(FlightRequestDto flight) {
        try {
            // Flight Id
            if (flight.getFlightId() == null || flight.getFlightId().isBlank()) {
                return ResponseEntity.status(400).body("Flight id cannot be null or empty.");
            } else if (!flight.getFlightId().matches(alphanumericRegexWithSpace)) {
                return ResponseEntity.status(400).body("Flight id must consist of alphanumeric characters only.");
            } else {
                flight.setFlightId(flight.getFlightId().toUpperCase());
                if (flightRepository.existsByFlightId(flight.getFlightId())) {
                    return ResponseEntity.status(400).body("Flight with provided flight id already exists.");
                }
            }

            // Departure airport
            if (flight.getDepartureAirportId() == null) {
                return ResponseEntity.status(400).body("Departure airport id cannot be null.");
            }
            Airport departureAirport;
            if (!airportRepository.existsById(flight.getDepartureAirportId())) {
                return ResponseEntity.status(404).body("Departure airport with provided id not found.");
            } else {
                departureAirport = airportRepository.findById(flight.getDepartureAirportId()).get();
            }

            // Arrival airport
            if (flight.getArrivalAirportId() == null) {
                return ResponseEntity.status(400).body("Arrival airport id cannot be null.");
            }
            Airport arrivalAirport;
            if (!airportRepository.existsById(flight.getArrivalAirportId())) {
                return ResponseEntity.status(404).body("Arrival airport with provided id not found.");
            } else {
                arrivalAirport = airportRepository.findById(flight.getArrivalAirportId()).get();
            }

            // Airports validation
            if (flight.getDepartureAirportId().equals(flight.getArrivalAirportId())) {
                return ResponseEntity.status(400).body("Departure and arrival airport cannot be the same.");
            }

            // Airline
            if (flight.getAirlineId() == null) {
                return ResponseEntity.status(400).body("Airline id cannot be null.");
            }
            Airline airline;
            if (!airlineRepository.existsById(flight.getAirlineId())) {
                return ResponseEntity.status(404).body("Airline with provided id not found.");
            } else {
                airline = airlineRepository.findById(flight.getAirlineId()).get();
            }

            // Save
            Flight saved = flightRepository.save(new Flight(
                    -1,
                    flight.getFlightId(),
                    departureAirport,
                    arrivalAirport,
                    airline,
                    null
            ));

            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    public ResponseEntity<?> updateFlight(FlightRequestDto flight) {
        try {
            // Validation
            Flight existing;
            if (flight.getId() != null && flightRepository.existsById(flight.getId())) {
                existing = flightRepository.findById(flight.getId()).get();
            } else {
                return ResponseEntity.status(404).body("Flight with provided id not found.");
            }

            // Flight Id
            if (flight.getFlightId() != null && !flight.getFlightId().isBlank()) {
                if (!flight.getFlightId().matches(alphanumericRegexWithSpace)) {
                    return ResponseEntity.status(400).body("Flight id must consist of alphanumeric characters only.");
                }
                if (!existing.getFlightId().equals(flight.getFlightId()) && flightRepository.existsByFlightId(flight.getFlightId())) {
                    return ResponseEntity.status(400).body("Flight with provided flight id already exists.");
                } else {
                    existing.setFlightId(flight.getFlightId());
                }
            }

            // Departure airport
            if (flight.getDepartureAirportId() != null && flight.getDepartureAirportId() != existing.getDepartureAirport().getId()) {
                if (!airportRepository.existsById(flight.getDepartureAirportId())) {
                    return ResponseEntity.status(404).body("Departure airport with provided id not found.");
                } else {
                    existing.setDepartureAirport(airportRepository.findById(flight.getDepartureAirportId()).get());
                }
            }

            // Arrival airport
            if (flight.getArrivalAirportId() != null && flight.getArrivalAirportId() != existing.getArrivalAirport().getId()) {
                if (!airportRepository.existsById(flight.getArrivalAirportId())) {
                    return ResponseEntity.status(404).body("Arrival airport with provided id not found.");
                } else {
                    existing.setArrivalAirport(airportRepository.findById(flight.getArrivalAirportId()).get());
                }
            }

            // Airports validation
            if (existing.getDepartureAirport().equals(existing.getArrivalAirport())) {
                return ResponseEntity.status(400).body("Departure and arrival airport cannot be the same.");
            }

            // Airline
            if (flight.getAirlineId() != null && flight.getAirlineId() != existing.getAirline().getId()) {
                if (!airlineRepository.existsById(flight.getAirlineId())) {
                    return ResponseEntity.status(404).body("Airline with provided id not found.");
                } else {
                    existing.setAirline(airlineRepository.findById(flight.getAirlineId()).get());
                }
            }

            // Save
            existing = flightRepository.save(existing);

            return ResponseEntity.ok(existing);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    public ResponseEntity<?> getAllFlights() {
        try {
            // Get all
            Iterable<Flight> flights = flightRepository.findAllSorted();

            return ResponseEntity.ok(flights);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    public ResponseEntity<?> getFlightByFlightId(Integer id) {
        try {
            // Validation
            if (!flightRepository.existsById(id)) {
                return ResponseEntity.status(404).body("Flight not found.");
            }

            // Get by id
            Flight flight = flightRepository.findById(id).get();

            return ResponseEntity.ok(flight);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    public ResponseEntity<?> deleteFlightByFlightId(Integer id) {
        try {
            // Validation
            if (!flightRepository.existsById(id)) {
                return ResponseEntity.status(404).body("Flight not found.");
            }

            // Delete
            flightRepository.deleteById(id);

            return ResponseEntity.ok(new JsonMessage("Flight deleted."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }
}
