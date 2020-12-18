package com.ns.awp.ticket.models.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class BulkTicketSaveRequestDto {
    @NonNull
    private Integer flightInstanceId;

    @NonNull
    private Integer count;
}
