package com.ns.awp.reservation.models.dto;

import com.ns.awp.reservation.models.Reservation;
import com.ns.awp.ticket.models.dto.TicketReservationResponseDto;
import lombok.Data;

@Data
public class ReservationResponseDto {
    private int id;
    private TicketReservationResponseDto departureTicket;
    private TicketReservationResponseDto returnTicket;
    private Long reservationDate;

    public ReservationResponseDto(Reservation reservation) {
        this.id = reservation.getId();
        this.departureTicket = new TicketReservationResponseDto(reservation.getDepartureTicket());
        this.returnTicket = reservation.getReturnTicket() != null ? new TicketReservationResponseDto(reservation.getReturnTicket()) : null;
        this.reservationDate = reservation.getReservationDate().getTime();
    }
}
