package com.ns.awp.flightInstance.models.dto;

import lombok.Data;

@Data
public class FlightInstanceSaveRequestDto {
    private Integer id;
    private String flightId;
    private Long flightDate;
    private Integer flightLengthInMinutes;
    private Integer count;
}
