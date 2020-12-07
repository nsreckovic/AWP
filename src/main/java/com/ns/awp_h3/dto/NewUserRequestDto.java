package com.ns.awp_h3.dto;

import lombok.Data;

@Data
public class NewUserRequestDto {
    private String username;
    private String password;
    private String name;
    private String lastName;
    private int userType;
    private int studentGroup;
}
