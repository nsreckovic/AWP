package com.ns.awp.ticket.models.dto;

import com.ns.awp.flightInstance.models.dto.FlightInstanceResponseDto;
import com.ns.awp.ticket.models.Ticket;
import lombok.Data;

import java.util.Objects;

@Data
public class TicketWithoutUserResponseDto {
    private int id;
    private FlightInstanceResponseDto flightInstance;

    public TicketWithoutUserResponseDto(Ticket ticket) {
        this.id = ticket.getId();
        this.flightInstance = new FlightInstanceResponseDto(ticket.getFlightInstance());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketWithoutUserResponseDto that = (TicketWithoutUserResponseDto) o;
        return flightInstance.equals(that.flightInstance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flightInstance);
    }
}
