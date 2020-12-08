package com.ns.awp_h3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDto {
    private Integer id;
    private String username;
    private String name;
    private String lastName;
    private UserTypeResponseDto userType;
    private StudentGroupResponseDto studentGroup;
}
