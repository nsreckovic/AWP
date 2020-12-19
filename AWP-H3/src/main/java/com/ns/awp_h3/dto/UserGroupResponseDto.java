package com.ns.awp_h3.dto;

import com.ns.awp_h3.models.UserGroup;
import lombok.Data;

@Data
public class UserGroupResponseDto {
    private int id;
    private String name;

    public UserGroupResponseDto(UserGroup userGroup) {
        this.id = userGroup.getId();
        this.name = userGroup.getName();
    }
}
