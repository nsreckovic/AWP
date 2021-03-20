package com.ns.awp.flightInstance.models.dto;

import com.ns.awp.flight.models.Flight;
import com.ns.awp.flightInstance.models.FlightInstance;
import lombok.Data;

@Data
public class FlightInstanceResponseDto {
    private int id;
    private Flight flight;
    private Long flightDate;
    private int flightLengthInMinutes;
    private int count;
    private int ticketCount;

    public FlightInstanceResponseDto(FlightInstance flightInstance) {
        this.id = flightInstance.getId();
        this.flight = flightInstance.getFlight();
        this.flightLengthInMinutes = flightInstance.getFlightLengthInMinutes();
        this.flightDate = flightInstance.getFlightDate().getTime();
        this.count = flightInstance.getCount();
        this.ticketCount = flightInstance.getTickets() != null ? flightInstance.getTickets().size() : 0;
    }
}
