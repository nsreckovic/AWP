package com.ns.awp.ticket.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ns.awp.flightInstance.models.FlightInstance;
import com.ns.awp.reservation.models.Reservation;
import com.ns.awp.user.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    private FlightInstance flightInstance;

    @JsonIgnore
    @ManyToOne()
    private User user;

    @JsonIgnore
    @OneToOne(orphanRemoval = true, mappedBy = "departureTicket")
    private Reservation reservation;

    @JsonIgnore
    @OneToOne(orphanRemoval = true, mappedBy = "returnTicket")
    private Reservation reservation1;
}
