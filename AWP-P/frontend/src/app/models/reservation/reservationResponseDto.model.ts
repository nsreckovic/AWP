import TicketWithoutUserResponseDto from "../ticket/ticketWithoutUserResponseDto.model copy";
import UserResponseDto from "../user/userResponseDto.model";

export default class ReservationResponseDto {
  constructor(
    public id: number,
    public user: UserResponseDto,
    public departureTicket: TicketWithoutUserResponseDto,
    public returnTicket: TicketWithoutUserResponseDto,
    public reservationDate: number
  ) {}
}
