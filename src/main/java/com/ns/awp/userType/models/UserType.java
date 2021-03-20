package com.ns.awp.userType.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ns.awp.user.models.User;
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

    @JsonIgnore
    @OneToMany(orphanRemoval = true, mappedBy = "userType")
    private List<User> users;
}
