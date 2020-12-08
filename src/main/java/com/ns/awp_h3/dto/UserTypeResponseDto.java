package com.ns.awp_h3.dto;

import com.ns.awp_h3.models.UserType;
import lombok.Data;

@Data
public class UserTypeResponseDto {
    private int id;
    private String name;

    public UserTypeResponseDto(UserType userType) {
        this.id = userType.getId();
        this.name = userType.getName();
    }
}
