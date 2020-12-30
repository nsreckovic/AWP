package com.ns.awp.user.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ns.awp.reservation.models.dto.ReservationResponseDto;
import com.ns.awp.ticket.models.dto.TicketWithoutUserResponseDto;
import com.ns.awp.user.models.User;
import com.ns.awp.userType.models.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class UserResponseDto {
    private Integer id;
    private String username;
    private String name;
    private String lastName;
    private UserType userType;
    @JsonIgnore
    private List<ReservationResponseDto> reservations;
    @JsonIgnore
    private List<TicketWithoutUserResponseDto> tickets;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.lastName = user.getLastName();
        this.userType = user.getUserType();
        //this.reservations = user.getReservations() != null ? user.getReservations().stream().map(ReservationResponseDto::new).collect(Collectors.toList()) : null;
        //this.tickets = user.getTickets() != null ? user.getTickets().stream().map(TicketWithoutUserResponseDto::new).collect(Collectors.toList()) : null;
    }
}
