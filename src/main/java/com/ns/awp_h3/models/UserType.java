package com.ns.awp_h3.models;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class UserType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(orphanRemoval = true, mappedBy = "userType")
    private List<User> users;
}
