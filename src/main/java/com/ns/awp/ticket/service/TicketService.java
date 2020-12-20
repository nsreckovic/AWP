package com.ns.awp.ticket.service;

import com.ns.awp.ticket.models.dto.Filter;
import com.ns.awp.ticket.models.dto.TicketSaveRequestDto;
import org.springframework.http.ResponseEntity;

public interface TicketService {
    ResponseEntity<?> newTicket(TicketSaveRequestDto ticket);

    ResponseEntity<?> updateTicket(TicketSaveRequestDto ticket);

    ResponseEntity<?> getAllTickets(Filter filter);

    ResponseEntity<?> getAllAvailableFromTicketsByFilter(Filter filter);

    ResponseEntity<?> getAllAvailableReturnTicketsByFilter(Filter filter);

    ResponseEntity<?> getTicketById(int id);

    ResponseEntity<?> deleteTicketById(int id);
}
