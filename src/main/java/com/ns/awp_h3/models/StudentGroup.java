package com.ns.awp_h3.models;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class StudentGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(orphanRemoval = true, mappedBy = "studentGroup")
    private List<User> users;
}
