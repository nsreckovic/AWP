package com.ns.awp.ticket.repository;

import com.ns.awp.ticket.models.Ticket;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;

public interface TicketRepository extends CrudRepository<Ticket, Integer> {
    @Query("SELECT t FROM Ticket t " +
            "LEFT JOIN t.user u " +
            "JOIN t.flightInstance fi " +
            "JOIN t.flightInstance.flight f " +
            "WHERE " +
            "(u.id IS NULL) AND " +
            "((:fromDate IS NULL OR :fromDate <= fi.flightDate) AND " +
            "(:toDate IS NULL OR :toDate >= fi.flightDate)) AND " +
            "(:fromAirportId IS NULL OR :fromAirportId = f.departureAirport.airportId) AND " +
            "(:toAirportId IS NULL OR :toAirportId = f.arrivalAirport.airportId) AND " +
            "(:airlineId IS NULL OR :airlineId = f.airline.id)")
    Iterable<Ticket> findAllAvailableTicketsByFilter(
            @Param("fromDate") Timestamp fromDate,
            @Param("toDate") Timestamp toDate,
            @Param("fromAirportId") String fromAirportId,
            @Param("toAirportId") String toAirportId,
            @Param("airlineId") Integer airlineId);


    @Query("SELECT t FROM Ticket t " +
            "LEFT JOIN t.user u " +
            "JOIN t.flightInstance fi " +
            "JOIN t.flightInstance.flight f " +
            "WHERE " +
            "(:userId IS NULL OR :userId = u.id) AND " +
            "((:fromDate IS NULL OR :fromDate <= fi.flightDate) AND " +
            "(:toDate IS NULL OR :toDate >= fi.flightDate)) AND " +
            "(:fromAirportId IS NULL OR :fromAirportId = f.departureAirport.airportId) AND " +
            "(:toAirportId IS NULL OR :toAirportId = f.arrivalAirport.airportId) AND " +
            "(:airlineId IS NULL OR :airlineId = f.airline.id)")
    Iterable<Ticket> findAllUserTicketsByFilter(
            @Param("userId") Integer userId,
            @Param("fromDate") Timestamp fromDate,
            @Param("toDate") Timestamp toDate,
            @Param("fromAirportId") String fromAirportId,
            @Param("toAirportId") String toAirportId,
            @Param("airlineId") Integer airlineId);
}
