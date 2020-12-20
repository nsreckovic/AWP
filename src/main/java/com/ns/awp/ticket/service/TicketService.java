package com.ns.awp.ticket.service;

import com.ns.awp.ticket.models.dto.TicketFilter;
import com.ns.awp.ticket.models.dto.TicketSaveRequestDto;
import org.springframework.http.ResponseEntity;

public interface TicketService {
    ResponseEntity<?> newTicket(TicketSaveRequestDto ticket);

    ResponseEntity<?> updateTicket(TicketSaveRequestDto ticket);

    ResponseEntity<?> getAllTickets(TicketFilter filter);

    ResponseEntity<?> getAllAvailableFromTicketsByFilter(TicketFilter filter);

    ResponseEntity<?> getAllAvailableReturnTicketsByFilter(TicketFilter filter);

    ResponseEntity<?> getTicketById(int id);

    ResponseEntity<?> deleteTicketById(int id);
}
