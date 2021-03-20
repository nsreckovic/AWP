package com.ns.awp.user.models.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class UserLoginRequestDto {
    @NonNull
    private String username;

    @NonNull
    private String password;
}
