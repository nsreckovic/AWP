package com.ns.awp.ticket.controller;

import com.ns.awp.ticket.models.dto.TicketFilter;
import com.ns.awp.ticket.models.dto.TicketRequestDto;
import com.ns.awp.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> newTicket(@RequestBody TicketRequestDto ticket) {
        return ticketService.newTicket(ticket);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping
    public ResponseEntity<?> updateTicket(@RequestBody TicketRequestDto ticket) {
        return ticketService.updateTicket(ticket);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'REGULAR')")
    @PostMapping("/all")
    public ResponseEntity<?> getAllTickets(@RequestBody TicketFilter filter) {
        return ticketService.getAllTickets(filter);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'REGULAR')")
    @PostMapping("/from")
    public ResponseEntity<?> getAllAvailableFromTicketsByFilter(@RequestBody TicketFilter filter) {
        return ticketService.getAllAvailableFromTicketsByFilter(filter);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'REGULAR')")
    @PostMapping("/return")
    public ResponseEntity<?> getAllAvailableReturnTicketsByFilter(@RequestBody TicketFilter filter) {
        return ticketService.getAllAvailableReturnTicketsByFilter(filter);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTicketById(@PathVariable("id") int id) {
        return ticketService.getTicketById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTicketById(@PathVariable("id") int id) {
        return ticketService.deleteTicketById(id);
    }
}
