package com.ns.awp.reservation.models.dto;

import lombok.Data;

@Data
public class ReservationFilter {
    private Integer userId;          // For admin use only
    private Long fromDate;           // FROM
    private Long toDate;             // FROM, RETURN
    private String fromAirportId;    // FROM
    private String toAirportId;      // FROM
    private Integer airlineId;       // FROM, RETURN
}
