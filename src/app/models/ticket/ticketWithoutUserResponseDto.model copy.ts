import Flight from "../flight/flight.model";

export default class TicketWithoutUserResponseDto {
  constructor(
    public id: number,
    public flight: Flight,
    public flightDate: number
  ) {}
}
