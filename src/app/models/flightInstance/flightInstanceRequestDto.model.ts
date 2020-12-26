export default class FlightInstanceRequestDto {
  constructor(
    public id: number,
    public flightId: number,
    public flightDate: number,
    public flightLengthInMinutes: number,
    public count: number
  ) {}
}
