package com.ns.awp.ticket.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ns.awp.flightInstance.models.FlightInstance;
import com.ns.awp.reservation.models.Reservation;
import com.ns.awp.user.models.User;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonIgnore
    @ManyToOne(optional = false)
    private FlightInstance flightInstance;

    @JsonIgnore
    @ManyToOne(optional = false)
    private User user;

    @OneToMany(orphanRemoval = true, mappedBy = "departureTicket")
    private List<Reservation> reservations;

    @OneToMany(orphanRemoval = true, mappedBy = "returnTicket")
    private List<Reservation> reservations1;
}
