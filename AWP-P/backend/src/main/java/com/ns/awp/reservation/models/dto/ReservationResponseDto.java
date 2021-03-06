package com.ns.awp.reservation.models.dto;

import com.ns.awp.reservation.models.Reservation;
import com.ns.awp.ticket.models.dto.TicketWithoutUserResponseDto;
import com.ns.awp.user.models.dto.UserResponseDto;
import lombok.Data;

@Data
public class ReservationResponseDto {
    private int id;
    private UserResponseDto user;
    private TicketWithoutUserResponseDto departureTicket;
    private TicketWithoutUserResponseDto returnTicket;
    private Long reservationDate;

    public ReservationResponseDto(Reservation reservation) {
        this.id = reservation.getId();
        this.user = new UserResponseDto(reservation.getUser());
        this.departureTicket = new TicketWithoutUserResponseDto(reservation.getDepartureTicket());
        this.returnTicket = reservation.getReturnTicket() != null ? new TicketWithoutUserResponseDto(reservation.getReturnTicket()) : null;
        this.reservationDate = reservation.getReservationDate().getTime();
    }
}
