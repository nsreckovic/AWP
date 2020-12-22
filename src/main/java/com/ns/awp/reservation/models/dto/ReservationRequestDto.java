package com.ns.awp.reservation.models.dto;

import lombok.Data;

@Data
public class ReservationRequestDto {
    private Integer id;
    private Integer departureTicketId;
    private Integer returnTicketId;
    private Integer userId;
}
