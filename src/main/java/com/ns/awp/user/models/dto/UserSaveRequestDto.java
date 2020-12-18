package com.ns.awp.user.models.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class UserSaveRequestDto {
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
}
