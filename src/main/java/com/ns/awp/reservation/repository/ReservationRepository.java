package com.ns.awp.reservation.repository;

import com.ns.awp.reservation.models.Reservation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;

public interface ReservationRepository extends CrudRepository<Reservation, Integer> {
    @Query("SELECT r FROM Reservation r " +
            "LEFT JOIN r.departureTicket dt " +
            "LEFT JOIN r.returnTicket rt " +
            "LEFT JOIN dt.user u " +
            "JOIN dt.flightInstance dfi " +
            "JOIN dfi.flight df " +
            "LEFT JOIN rt.flightInstance rfi " +
            "LEFT JOIN rfi.flight rf " +
            "WHERE " +
            "(:userId IS NULL OR :userId = u.id) AND " +
            "((:fromDate IS NULL OR :fromDate <= dfi.flightDate) AND " +
            "(rt IS NULL OR (:toDate IS NULL OR :toDate >= rfi.flightDate)) AND " +
            "(:toDate IS NULL OR :toDate >= dfi.flightDate)) AND " +
            "(:fromAirportId IS NULL OR :fromAirportId = df.departureAirport.id) AND " +
            "(:toAirportId IS NULL OR :toAirportId = df.arrivalAirport.id) AND " +
            "(:airlineId IS NULL OR :airlineId = df.airline.id OR :airlineId = rf.airline.id)")
    Iterable<Reservation> findAllUserReservationsByFilter(
            @Param("userId") Integer userId,
            @Param("fromDate") Timestamp fromDate,
            @Param("toDate") Timestamp toDate,
            @Param("fromAirportId") Integer fromAirportId,
            @Param("toAirportId") Integer toAirportId,
            @Param("airlineId") Integer airlineId);
}
