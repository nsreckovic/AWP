export default class TicketRequestDto {
  constructor(
    public id: number,
    public flightInstanceId: number,
    public ticketCount: number
  ) {}
}
