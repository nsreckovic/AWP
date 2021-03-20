package com.ns.awp.reservation.models.dto;

import lombok.Data;

@Data
public class ReservationFilter {
    private Integer userId;
    private Long fromDate;
    private Long toDate;
    private Integer fromAirportId;
    private Integer toAirportId;
    private Integer airlineId;
    private String type;
}
