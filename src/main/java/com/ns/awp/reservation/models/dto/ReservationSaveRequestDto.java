package com.ns.awp.reservation.models.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class ReservationSaveRequestDto {
    private Integer id;
    private Integer departureTicketId;
    private Integer returnTicketId;
    private Integer userId;
}
