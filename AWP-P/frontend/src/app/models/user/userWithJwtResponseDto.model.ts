import UserResponseDto from './userResponseDto.model';

export default class UserWithJwtResponseDto {
  constructor(public user: UserResponseDto, public jwt: string) {}
}
