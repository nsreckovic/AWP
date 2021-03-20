import UserType from '../userType/userType.model';

export default class UserResponseDto {
  constructor(
    public id: number,
    public username: string,
    public name: string,
    public lastName: string,
    public userType: UserType,
    public ticketCount: number,
    public reservationCount: number
  ) {}
}
