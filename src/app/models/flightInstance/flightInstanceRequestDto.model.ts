export default class FlightInstanceRequestDto {
  constructor(
    public id: number,
    public flightId: string,
    public flightDate: number,
    public flightLengthInMinutes: number,
    public count: number
  ) {}
}
