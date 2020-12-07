package com.ns.awp_h3.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import org.springframework.web.bind.annotation.PathVariable;

import javax.persistence.*;

@Entity
@Data
public class UserType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;
}
