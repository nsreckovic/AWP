package com.ns.awp.flightInstance.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ns.awp.flight.models.Flight;
import com.ns.awp.ticket.models.Ticket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor
public class FlightInstance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonIgnore
    @ManyToOne(optional = false)
    private Flight flight;

    @Column(nullable = false)
    private Timestamp flightDate;

    @Column(nullable = false)
    private Integer flightLengthInMinutes;

    @Column(nullable = false)
    private Integer count;

    @JsonIgnore
    @OneToMany(orphanRemoval = true, mappedBy = "flightInstance")
    private List<Ticket> tickets;

    public void incrementCount() {
        ++this.count;
    }

    public void decrementCount() {
        --this.count;
    }

    public void bulkDecrementCount(int count) {
        this.count -= count;
    }
}
