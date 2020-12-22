package com.ns.awp.ticket.models.dto;

import lombok.Data;

@Data
public class TicketRequestDto {
    private Integer id;
    private Integer flightInstanceId;
    private Integer ticketCount;
}
