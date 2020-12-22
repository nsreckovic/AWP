import ReservationResponseDto from '../reservation/reservationResponseDto.model';
import TicketWithoutUserResponseDto from '../ticket/ticketWithoutUserResponseDto.model copy';
import UserTypeResponseDto from '../userType/userTypeResponseDto.model';

export default class UserResponseDto {
  constructor(
    public id: number,
    public username: string,
    public name: string,
    public lastName: string,
    public userType: UserTypeResponseDto,
    public reservations: ReservationResponseDto[],
    public tickets: TicketWithoutUserResponseDto[]
  ) {}
}
