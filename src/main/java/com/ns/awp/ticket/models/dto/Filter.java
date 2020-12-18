package com.ns.awp.ticket.models.dto;

import lombok.Data;

@Data
public class Filter {
    private Integer userId;                 // For admins only
    private Long fromDate;                  // FROM
    private Long toDate;                    // FROM, RETURN
    private String fromAirportId;           // FROM
    private String toAirportId;             // FROM
    private Integer airlineId;              // FROM, RETURN
    private Integer currentFromTicketId;    // RETURN
}
