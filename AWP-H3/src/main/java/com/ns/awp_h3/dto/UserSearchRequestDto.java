package com.ns.awp_h3.dto;

import lombok.Data;

@Data
public class UserSearchRequestDto {
    private String username;
    private String name;
    private String lastName;
    private String userType;
    private String userGroup;
}
