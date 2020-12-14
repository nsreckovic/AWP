package com.ns.awp.airport.models;

import com.ns.awp.flight.models.Flight;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Airport {
    @Id
    @Column(nullable = false, unique = true)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String place;

    @OneToMany(orphanRemoval = true, mappedBy = "departureAirport")
    private List<Flight> flights;

    @OneToMany(orphanRemoval = true, mappedBy = "arrivalAirport")
    private List<Flight> flights1;
}
