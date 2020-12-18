package com.ns.awp.ticket.models.dto;

import com.ns.awp.flight.models.Flight;
import com.ns.awp.ticket.models.Ticket;
import com.ns.awp.user.models.dto.UserTicketResponseDto;
import lombok.Data;

@Data
public class TicketUserResponseDto {
    private int id;
    private Flight flight;
    private Long flightDate;

    public TicketUserResponseDto(Ticket ticket) {
        this.id = ticket.getId();
        this.flight = ticket.getFlightInstance().getFlight();
        this.flightDate = ticket.getFlightInstance().getFlightDate().getTime();
    }
}

