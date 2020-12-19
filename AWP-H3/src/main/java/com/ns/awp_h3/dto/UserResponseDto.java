package com.ns.awp_h3.dto;

import com.ns.awp_h3.models.User;
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
    private UserGroupResponseDto userGroup;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.lastName = user.getLastName();
        this.userType = new UserTypeResponseDto(user.getUserType());
        this.userGroup = user.getUserGroup() != null ? new UserGroupResponseDto(user.getUserGroup()) : null;
    }
}
