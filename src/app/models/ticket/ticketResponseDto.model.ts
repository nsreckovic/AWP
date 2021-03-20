import Flight from '../flight/flight.model';
import FlightInstanceResponseDto from '../flightInstance/flightInstanceResponseDto.model';
import UserTicketResponseDto from '../user/userTicketResponseDto.model';

export default class TicketResponseDto {
  constructor(
    public id: number,
    public user: UserTicketResponseDto,
    public flightInstance: FlightInstanceResponseDto
  ) {}
}
