package com.ns.awp.ticket.models.dto;

import com.ns.awp.flight.models.Flight;
import com.ns.awp.flightInstance.models.dto.FlightInstanceResponseDto;
import com.ns.awp.ticket.models.Ticket;
import lombok.Data;

@Data
public class TicketWithoutUserResponseDto {
    private int id;
    private FlightInstanceResponseDto flightInstance;

    public TicketWithoutUserResponseDto(Ticket ticket) {
        this.id = ticket.getId();
        this.flightInstance = new FlightInstanceResponseDto(ticket.getFlightInstance());
    }
}
