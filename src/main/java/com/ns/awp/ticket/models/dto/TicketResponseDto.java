package com.ns.awp.ticket.models.dto;

import com.ns.awp.flight.models.Flight;
import com.ns.awp.ticket.models.Ticket;
import com.ns.awp.user.models.dto.UserTicketResponseDto;
import lombok.Data;

@Data
public class TicketResponseDto {
    private int id;
    private UserTicketResponseDto user;
    private Flight flight;
    private Long flightDate;

    public TicketResponseDto(Ticket ticket) {
        this.id = ticket.getId();
        this.user = ticket.getUser() != null ? new UserTicketResponseDto(ticket.getUser()) : null;
        this.flight = ticket.getFlightInstance().getFlight();
        this.flightDate = ticket.getFlightInstance().getFlightDate().getTime();
    }
}
