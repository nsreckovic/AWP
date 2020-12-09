package com.ns.awp_h3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserWithJwtDto {
    private UserResponseDto user;
    private TokenDto token;
}
