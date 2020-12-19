package com.ns.awp_h3.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class NewUserRequestDto {
    private Integer id;

    @NonNull
    private String username;

    @NonNull
    private String password;

    private String newPassword;

    @NonNull
    private String name;

    @NonNull
    private String lastName;

    @NonNull
    private int userType;

    private Integer userGroup;
}
