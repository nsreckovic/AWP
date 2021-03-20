package com.ns.awp.user.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserWithJwtResponseDto {
    private UserResponseDto user;
    private String jwt;
}
