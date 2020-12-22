import Flight from "../flight/flight.model";

export default class FlightInstanceResponseDto {
  constructor(
    public id: number,
    public flight: Flight,
    public flightDate: number,
    public flightLengthInMinutes: number,
    public count: number,
    public ticketCount: number
  ) {}
}
