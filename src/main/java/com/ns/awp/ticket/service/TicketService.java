package com.ns.awp.ticket.service;

import com.ns.awp.ticket.models.dto.TicketFilter;
import com.ns.awp.ticket.models.dto.TicketRequestDto;
import org.springframework.http.ResponseEntity;

public interface TicketService {
    ResponseEntity<?> newTicket(TicketRequestDto ticket);

    ResponseEntity<?> updateTicket(TicketRequestDto ticket);

    ResponseEntity<?> getAllTickets(TicketFilter filter);

    ResponseEntity<?> getAllAvailableFromTicketsByFilter(TicketFilter filter);

    ResponseEntity<?> getAllAvailableReturnTicketsByFilter(TicketFilter filter);

    ResponseEntity<?> getTicketById(int id);

    ResponseEntity<?> deleteTicketById(int id);
}
