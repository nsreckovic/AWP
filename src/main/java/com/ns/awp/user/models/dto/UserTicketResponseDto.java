package com.ns.awp.user.models.dto;

import com.ns.awp.user.models.User;
import lombok.Data;

@Data
public class UserTicketResponseDto {
    private Integer id;
    private String username;
    private String name;
    private String lastName;

    public UserTicketResponseDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.lastName = user.getLastName();
    }
}
