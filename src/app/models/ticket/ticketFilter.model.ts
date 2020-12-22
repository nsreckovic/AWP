export default class TicketFilter {
  constructor(
    public userId: number,
    public fromDate: number,
    public toDate: number,
    public fromAirportId: string,
    public toAirportId: string,
    public airlineId: number,
    public fromTicketId: number
  ) {}
}
