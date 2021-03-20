export default class ReservationRequestDto {
  constructor(
    public id: number,
    public departureTicketId: number,
    public returnTicketId: number,
    public userId: number
  ) {}
}
