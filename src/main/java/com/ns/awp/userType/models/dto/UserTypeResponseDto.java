package com.ns.awp.userType.models.dto;

import com.ns.awp.userType.models.UserType;
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
