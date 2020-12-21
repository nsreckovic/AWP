package com.ns.awp.ticket.controller;

import com.ns.awp.ticket.models.dto.TicketFilter;
import com.ns.awp.ticket.models.dto.TicketSaveRequestDto;
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
    @PostMapping("/")
    public ResponseEntity<?> newTicket(@RequestBody TicketSaveRequestDto ticket) {
        return ticketService.newTicket(ticket);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/")
    public ResponseEntity<?> updateTicket(@RequestBody TicketSaveRequestDto ticket) {
        return ticketService.updateTicket(ticket);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'REGULAR')")
    @PostMapping("/all")
    public ResponseEntity<?> getAllTickets(@RequestBody TicketFilter filter) {
        // TODO
        //  - admin check -> get all
        //  - regular check -> check if it's authenticated user in the filter for userId attribute
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
        // TODO
        //  - admin check -> get all
        //  - regular check -> check if it's authenticated user in the filter's from ticket
        return ticketService.getAllAvailableReturnTicketsByFilter(filter);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTicketById(@PathVariable("id") int id) {
        // TODO
        //  - admin check -> get ticket
        //  - regular check -> check if it's authenticated user in the ticket
        return ticketService.getTicketById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTicketById(@PathVariable("id") int id) {
        return ticketService.deleteTicketById(id);
    }
}
