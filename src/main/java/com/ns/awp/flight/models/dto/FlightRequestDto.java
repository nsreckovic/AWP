package com.ns.awp.flight.models.dto;

import lombok.Data;

@Data
public class FlightRequestDto {
    private Integer id;
    private String flightId;
    private Integer departureAirportId;
    private Integer arrivalAirportId;
    private Integer airlineId;
}
