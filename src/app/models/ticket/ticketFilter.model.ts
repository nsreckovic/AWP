export default class TicketFilter {
  constructor(
    public userId: number,
    public fromDate: number,
    public toDate: number,
    public fromAirportId: number,
    public toAirportId: number,
    public airlineId: number,
    public fromTicketId: number
  ) {}
}
