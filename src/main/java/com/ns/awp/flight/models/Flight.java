package com.ns.awp.flight.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ns.awp.airline.models.Airline;
import com.ns.awp.airport.models.Airport;
import com.ns.awp.flightInstance.models.FlightInstance;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Flight {
    @Id
    @Column(nullable = false, unique = true)
    private Integer id;

    @JsonIgnore
    @ManyToOne(optional = false)
    private Airport departureAirport;

    @JsonIgnore
    @ManyToOne(optional = false)
    private Airport arrivalAirport;

    @JsonIgnore
    @ManyToOne(optional = false)
    private Airline airline;

    @OneToMany(orphanRemoval = true, mappedBy = "flight")
    private List<FlightInstance> flightInstances;
}
