package com.ns.awp.ticket.models.dto;

import com.ns.awp.flight.models.Flight;
import com.ns.awp.ticket.models.Ticket;
import lombok.Data;

@Data
public class TicketWithoutUserResponseDto {
    private int id;
    private Flight flight;
    private Long flightDate;

    public TicketWithoutUserResponseDto(Ticket ticket) {
        this.id = ticket.getId();
        this.flight = ticket.getFlightInstance().getFlight();
        this.flightDate = ticket.getFlightInstance().getFlightDate().getTime();
    }
}
