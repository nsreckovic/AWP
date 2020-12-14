package com.ns.awp.user.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ns.awp.reservation.models.Reservation;
import com.ns.awp.ticket.models.Ticket;
import com.ns.awp.userType.models.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String username;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String lastName;

    @JsonIgnore
    @ManyToOne(optional = false)
    private UserType userType;

    @OneToMany(orphanRemoval = true, mappedBy = "user")
    private List<Reservation> reservations;

    @OneToMany(orphanRemoval = true, mappedBy = "user")
    private List<Ticket> tickets;
}
