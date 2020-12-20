package com.ns.awp.ticket.models.dto;

import lombok.Data;

@Data
public class TicketSaveRequestDto {
    private Integer id;
    private Integer flightInstanceId;
    private Integer ticketCount;
}
