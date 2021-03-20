import Flight from "../flight/flight.model";
import FlightInstanceResponseDto from "../flightInstance/flightInstanceResponseDto.model";

export default class TicketWithoutUserResponseDto {
  constructor(
    public id: number,
    public flightInstance: FlightInstanceResponseDto,
  ) {}
}
