export default class FlightRequestDto {
  constructor(
    public id: number,
    public flightId: string,
    public departureAirportId: string,
    public arrivalAirportId: string,
    public airlineId: number
  ) {}
}
