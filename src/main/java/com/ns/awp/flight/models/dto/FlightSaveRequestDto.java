package com.ns.awp.flight.models.dto;

import lombok.Data;

@Data
public class FlightSaveRequestDto {
    private Integer id;
    private String flightId;
    private String departureAirportId;
    private String arrivalAirportId;
    private Integer airlineId;
}
