import TicketWithoutUserResponseDto from "../ticket/ticketWithoutUserResponseDto.model copy";

export default class ReservationResponseDto {
  constructor(
    public id: number,
    public departureTicket: TicketWithoutUserResponseDto,
    public returnTicket: TicketWithoutUserResponseDto,
    public reservationDate: number
  ) {}
}
