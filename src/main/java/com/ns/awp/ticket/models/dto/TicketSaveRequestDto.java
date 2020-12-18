package com.ns.awp.ticket.models.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class TicketSaveRequestDto {
    private Integer id;

    @NonNull
    private Integer userId;

    @NonNull
    private Integer flightInstanceId;
}
