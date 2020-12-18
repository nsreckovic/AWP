package com.ns.awp.ticket.repository;

import com.ns.awp.ticket.models.Ticket;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;

public interface TicketRepository extends CrudRepository<Ticket, Integer> {
    @Query("SELECT t FROM Ticket t " +
            "JOIN t.user u " +
            "WHERE " +
            "u.id = :userId")
    Iterable<Ticket> findAllByUserId(int userId);

    @Query("SELECT t FROM Ticket t " +
            "JOIN t.flightInstance.flight f " +
            "WHERE " +
            "f.airline.id = :airlineId")
    Iterable<Ticket> findAllByAirlineId(int airlineId);

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
    Iterable<Ticket> findAllTicketsByFilter(@Param("userId") Integer userId, @Param("fromDate") Timestamp fromDate, @Param("toDate") Timestamp toDate, @Param("fromAirportId") String fromAirportId, @Param("toAirportId") String toAirportId, @Param("airlineId") Integer airlineId);

    /*
    @Query("SELECT t FROM Ticket t " +
            "JOIN t.user u " +
            "JOIN t.flightInstance fi " +
            "JOIN t.flightInstance.flight f " +
            "WHERE " +
            "(:userId IS NULL OR :userId = u.id) AND " +
            "(:currentFromTicketId IS NULL OR :currentFromTicketId <> t.id) AND " +
            "((:fromDate IS NULL OR :fromDate <= fi.flightDate) AND (:toDate IS NULL OR :toDate <= fi.flightDate)) AND " +
            "(:fromAirportId IS NULL OR :fromAirportId = f.departureAirport.airportId) AND " +
            "(:toAirportId IS NULL OR :toAirportId = f.arrivalAirport.airportId) AND " +
            "(:airlineId IS NULL OR :airlineId = f.airline.id)")
    Iterable<Ticket> findAllReturnTicketsByFilter(@Param("userId") Integer userId, @Param("fromDate") Timestamp fromDate, @Param("toDate") Timestamp toDate, @Param("fromAirportId") String fromAirportId, @Param("toAirportId") String toAirportId, @Param("airlineId") Integer airlineId, @Param("currentFromTicketId") Integer currentFromTicketId);
     */

}
