package com.ns.awp.airline.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ns.awp.flight.models.Flight;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Airline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String link;

    @JsonIgnore
    @OneToMany(orphanRemoval = true, mappedBy = "airline")
    private List<Flight> flights;
}
