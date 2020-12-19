package com.ns.awp_h3.models;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class UserGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "userGroup")
    private List<User> users;
}
