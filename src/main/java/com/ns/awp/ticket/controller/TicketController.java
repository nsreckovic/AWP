package com.ns.awp.ticket.controller;

import com.ns.awp.ticket.models.dto.BulkTicketSaveRequestDto;
import com.ns.awp.ticket.models.dto.Filter;
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
    @PostMapping("/bulk")
    public ResponseEntity<?> newTickets(@RequestBody BulkTicketSaveRequestDto bulkRequest) {
        return ticketService.newBulkTicket(bulkRequest);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/")
    public ResponseEntity<?> updateTicket(@RequestBody TicketSaveRequestDto ticket) {
        return ticketService.updateTicket(ticket);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllTickets() {
        return ticketService.getAllTickets();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getAllTicketsByUserId(@PathVariable("id") int userId) {
        return ticketService.getAllTicketsByUser(userId);
    }

    @GetMapping("/airline/{id}")
    public ResponseEntity<?> getAllTicketsByAirlineId(@PathVariable("id") int airlineId) {
        return ticketService.getAllTicketsByAirline(airlineId);
    }

    @PostMapping("/from")
    public ResponseEntity<?> getAllFromTicketsByFilter(@RequestBody Filter filter) {
        return ticketService.getAllFromTicketsByFilter(filter);
    }

    @PostMapping("/return")
    public ResponseEntity<?> getAllReturnTicketsByFilter(@RequestBody Filter filter) {
        return ticketService.getAllReturnTicketsByFilter(filter);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTicketById(@PathVariable("id") int id) {
        return ticketService.getTicketById(id);
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTicketById(@PathVariable("id") int id) {
        return ticketService.deleteTicketById(id);
    }
}
