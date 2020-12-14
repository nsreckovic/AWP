package com.ns.awp.flightInstance.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ns.awp.flight.models.Flight;
import com.ns.awp.ticket.models.Ticket;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
public class FlightInstance {
    @Id
    @Column(nullable = false, unique = true)
    private Integer id;

    @JsonIgnore
    @ManyToOne(optional = false)
    private Flight flight;

    @Column(nullable = false)
    private Timestamp flightDate;

    @Column(nullable = false)
    private Integer count;

    @OneToMany(orphanRemoval = true, mappedBy = "flightInstance")
    private List<Ticket> tickets;
}
