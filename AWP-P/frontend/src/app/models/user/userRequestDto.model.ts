export default class UserRequestDto {
  constructor(
    public id: number,
    public username: string,
    public password: string,
    public newPassword: string,
    public name: string,
    public lastName: string,
    public userType: string
  ) {}
}
