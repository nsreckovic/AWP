export default class ReservationFilter {
  constructor(
    public userId: number,
    public fromDate: number,
    public toDate: number,
    public fromAirportId: string,
    public toAirportId: string,
    public airlineId: number,
    public type: string
  ) {}
}
