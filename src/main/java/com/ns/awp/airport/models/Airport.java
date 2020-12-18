package com.ns.awp.airport.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ns.awp.flight.models.Flight;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Airport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String airportId;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String place;

    @JsonIgnore
    @OneToMany(orphanRemoval = true, mappedBy = "departureAirport")
    private List<Flight> flights;

    @JsonIgnore
    @OneToMany(orphanRemoval = true, mappedBy = "arrivalAirport")
    private List<Flight> flights1;
}
