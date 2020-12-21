package com.ns.awp.user.models.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class UserSaveRequestDto {
    private Integer id;
    private String username;
    private String password;
    private String newPassword;
    private String name;
    private String lastName;
    private String userType;
}
