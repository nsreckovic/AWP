package com.ns.awp.flightInstance.models.dto;

import lombok.Data;

@Data
public class FlightInstanceRequestDto {
    private Integer id;
    private Integer flightId;
    private Long flightDate;
    private Integer flightLengthInMinutes;
    private Integer count;
}
