export default class FlightRequestDto {
  constructor(
    public id: number,
    public flightId: string,
    public departureAirportId: number,
    public arrivalAirportId: number,
    public airlineId: number
  ) {}
}
