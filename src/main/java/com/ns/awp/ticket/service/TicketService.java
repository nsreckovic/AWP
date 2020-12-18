package com.ns.awp.ticket.service;

import com.ns.awp.ticket.models.dto.BulkTicketSaveRequestDto;
import com.ns.awp.ticket.models.dto.Filter;
import com.ns.awp.ticket.models.dto.TicketSaveRequestDto;
import org.springframework.http.ResponseEntity;

public interface TicketService {
    ResponseEntity<?> newTicket(TicketSaveRequestDto ticket);

    ResponseEntity<?> newBulkTicket(BulkTicketSaveRequestDto bulkRequest);

    ResponseEntity<?> updateTicket(TicketSaveRequestDto ticket);

    ResponseEntity<?> getAllTickets();

    ResponseEntity<?> getAllTicketsByUser(int userId);

    ResponseEntity<?> getAllTicketsByAirline(int airlineId);

    ResponseEntity<?> getAllFromTicketsByFilter(Filter filter);

    ResponseEntity<?> getAllReturnTicketsByFilter(Filter filter);

    ResponseEntity<?> getTicketById(int id);

    ResponseEntity<?> deleteTicketById(int id);
}
