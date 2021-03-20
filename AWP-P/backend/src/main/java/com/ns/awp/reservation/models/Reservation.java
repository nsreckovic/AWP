package com.ns.awp.reservation.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ns.awp.ticket.models.Ticket;
import com.ns.awp.user.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonIgnore
    @OneToOne(optional = false)
    private Ticket departureTicket;

    @JsonIgnore
    @OneToOne()
    private Ticket returnTicket;

    @Column(nullable = false)
    private Timestamp reservationDate;

    @JsonIgnore
    @ManyToOne(optional = false)
    private User user;
}
