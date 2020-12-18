package com.ns.awp.user.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserWithJwtDto {
    private UserResponseDto user;
    private JwtDto jwt;
}
