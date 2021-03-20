package com.ns.awp.flight.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ns.awp.airline.models.Airline;
import com.ns.awp.airport.models.Airport;
import com.ns.awp.flightInstance.models.FlightInstance;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String flightId;

    @ManyToOne(optional = false)
    private Airport departureAirport;

    @ManyToOne(optional = false)
    private Airport arrivalAirport;

    @ManyToOne(optional = false)
    private Airline airline;

    @JsonIgnore
    @OneToMany(orphanRemoval = true, mappedBy = "flight")
    private List<FlightInstance> flightInstances;
}
