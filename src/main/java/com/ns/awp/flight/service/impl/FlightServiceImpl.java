package com.ns.awp.flight.service.impl;

import com.ns.awp.airline.models.Airline;
import com.ns.awp.airline.repository.AirlineRepository;
import com.ns.awp.airport.models.Airport;
import com.ns.awp.airport.repository.AirportRepository;
import com.ns.awp.config.JsonMessage;
import com.ns.awp.flight.models.Flight;
import com.ns.awp.flight.models.dto.FlightRequestDto;
import com.ns.awp.flight.repository.FlightRepository;
import com.ns.awp.flight.service.FlightService;
import com.ns.awp.flightInstance.repository.FlightInstanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {
    private final FlightRepository flightRepository;
    private final FlightInstanceRepository flightInstanceRepository;
    private final AirportRepository airportRepository;
    private final AirlineRepository airlineRepository;

    @Override
    public ResponseEntity<?> newFlight(FlightRequestDto flight) {
        try {
            // Flight id check
            if (flightRepository.existsByFlightId(flight.getFlightId())) {
                return ResponseEntity.status(400).body("Flight with provided flight id already exists.");
            }

            // Departure airport check
            Airport departureAirport;
            if (!airportRepository.existsById(flight.getDepartureAirportId())) {
                return ResponseEntity.status(404).body("Departure airport with provided id not found.");
            } else {
                departureAirport = airportRepository.findById(flight.getDepartureAirportId()).get();
            }

            // Arrival airport check
            Airport arrivalAirport;
            if (!airportRepository.existsById(flight.getArrivalAirportId())) {
                return ResponseEntity.status(404).body("Arrival airport with provided id not found.");
            } else {
                arrivalAirport = airportRepository.findById(flight.getArrivalAirportId()).get();
            }

            // Both airports check
            if (flight.getDepartureAirportId().equals(flight.getArrivalAirportId())) {
                return ResponseEntity.status(400).body("Departure and arrival airport cannot be the same.");
            }

            // Airline check
            Airline airline;
            if (!airlineRepository.existsById(flight.getAirlineId())) {
                return ResponseEntity.status(404).body("Airline with provided id not found.");
            } else {
                airline = airlineRepository.findById(flight.getAirlineId()).get();
            }

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

    @Override
    @Transactional
    public ResponseEntity<?> updateFlight(FlightRequestDto flight) {
        try {
            // Id check
            if (!flightRepository.existsById(flight.getId())) {
                return ResponseEntity.status(404).body("Flight with provided id not found.");
            }
            Flight existing = flightRepository.findById(flight.getId()).get();

            // Flight id check
            if (flight.getFlightId() != null) {
                if (!existing.getFlightId().equals(flight.getFlightId()) && flightRepository.existsByFlightId(flight.getFlightId())) {
                    return ResponseEntity.status(400).body("Flight with provided flight id already exists.");
                } else {
                    existing.setFlightId(flight.getFlightId());
                }
            }

            // Departure airport check
            if (flight.getDepartureAirportId() != null) {
                if (!airportRepository.existsById(flight.getDepartureAirportId())) {
                    return ResponseEntity.status(404).body("Departure airport with provided id not found.");
                } else {
                    existing.setDepartureAirport(airportRepository.findById(flight.getDepartureAirportId()).get());
                }
            }

            // Arrival airport check
            if (flight.getArrivalAirportId() != null) {
                if (!airportRepository.existsById(flight.getArrivalAirportId())) {
                    return ResponseEntity.status(404).body("Arrival airport with provided id not found.");
                } else {
                    existing.setArrivalAirport(airportRepository.findById(flight.getArrivalAirportId()).get());
                }
            }

            // Both airports check
            if (existing.getDepartureAirport().equals(existing.getArrivalAirport())) {
                return ResponseEntity.status(400).body("Departure and arrival airport cannot be the same.");
            }

            // Airline check
            if (!airlineRepository.existsById(flight.getAirlineId())) {
                return ResponseEntity.status(404).body("Airline with provided id not found.");
            } else {
                existing.setAirline(airlineRepository.findById(flight.getAirlineId()).get());
            }

            // Update flight instance id

            existing = flightRepository.save(existing);

            return ResponseEntity.ok(existing);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    @Override
    public ResponseEntity<?> getAllFlights() {
        try {
            // Getting all
            Iterable<Flight> flights = flightRepository.findAllSorted();

            return ResponseEntity.ok(flights);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    @Override
    public ResponseEntity<?> getFlightByFlightId(Integer id) {
        try {
            // Id check
            if (!flightRepository.existsById(id)) {
                return ResponseEntity.status(404).body("Flight not found.");
            }

            // Getting by id
            Flight flight = flightRepository.findById(id).get();

            return ResponseEntity.ok(flight);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    @Override
    public ResponseEntity<?> deleteFlightByFlightId(Integer id) {
        try {
            // Id check
            if (!flightRepository.existsById(id)) {
                return ResponseEntity.status(404).body("Flight not found.");
            }

            // Deleting
            flightRepository.deleteById(id);

            return ResponseEntity.ok(new JsonMessage("Flight deleted."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }
}
