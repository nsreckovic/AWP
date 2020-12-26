import Flight from '../flight/flight.model';
import UserTicketResponseDto from '../user/userTicketResponseDto.model';

export default class TicketResponseDto {
  constructor(
    public id: number,
    public user: UserTicketResponseDto,
    public flight: Flight,
    public flightDate: number,
    public flightLengthInMinutes: number
  ) {}
}
