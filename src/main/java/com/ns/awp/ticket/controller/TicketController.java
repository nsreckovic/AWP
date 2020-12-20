package com.ns.awp.ticket.controller;

import com.ns.awp.ticket.models.dto.TicketFilter;
import com.ns.awp.ticket.models.dto.TicketSaveRequestDto;
import com.ns.awp.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;

    //@PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<?> newTicket(@RequestBody TicketSaveRequestDto ticket) {
        return ticketService.newTicket(ticket);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/")
    public ResponseEntity<?> updateTicket(@RequestBody TicketSaveRequestDto ticket) {
        return ticketService.updateTicket(ticket);
    }

    @PostMapping("/all")
    public ResponseEntity<?> getAllTickets(@RequestBody TicketFilter filter) {
        // TODO
        //  - admin check -> get all
        //  - regular check -> check if it's authenticated user in the filter
        return ticketService.getAllTickets(filter);
    }

    @PostMapping("/from")
    public ResponseEntity<?> getAllAvailableFromTicketsByFilter(@RequestBody TicketFilter filter) {
        return ticketService.getAllAvailableFromTicketsByFilter(filter);
    }

    @PostMapping("/return")
    public ResponseEntity<?> getAllAvailableReturnTicketsByFilter(@RequestBody TicketFilter filter) {
        return ticketService.getAllAvailableReturnTicketsByFilter(filter);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTicketById(@PathVariable("id") int id) {
        // TODO
        //  - admin check -> get ticket
        //  - regular check -> check if it's authenticated user in the ticket
        return ticketService.getTicketById(id);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTicketById(@PathVariable("id") int id) {
        return ticketService.deleteTicketById(id);
    }
}
