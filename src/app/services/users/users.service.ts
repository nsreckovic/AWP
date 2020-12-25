import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { API_URL } from 'src/app/app.constants';
import JsonMessage from 'src/app/models/JsonMessage.models';
import UserRequestDto from 'src/app/models/user/userRequestDto.model';
import UserResponseDto from 'src/app/models/user/userResponseDto.model';
import UserWithJwtResponseDto from 'src/app/models/user/userWithJwtResponseDto.model';

@Injectable({
  providedIn: 'root'
})
export class UsersService {
  private readonly usersUrl = API_URL + '/users';

  constructor(private httpClient: HttpClient) { }

  newUser(user: UserRequestDto) {
    return this.httpClient.post<UserResponseDto>(`${this.usersUrl}/register`, user);
  }

  updateUser(user: UserRequestDto) {
    return this.httpClient.put<UserWithJwtResponseDto>(`${this.usersUrl}`, user);
  }

  getAllUsers() {
    return this.httpClient.get<UserResponseDto[]>(`${this.usersUrl}/all`);
  }

  getUserById(id: number) {
    return this.httpClient.get<UserResponseDto>(`${this.usersUrl}/${id}`);
  }

  deleteUserById(id: number) {
    return this.httpClient.delete<JsonMessage>(`${this.usersUrl}/${id}`);
  }
}
