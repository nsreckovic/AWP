export default class UserTicketResponseDto {
  constructor(
    public id: number,
    public username: string,
    public name: string,
    public lastName: string
  ) {}
}
