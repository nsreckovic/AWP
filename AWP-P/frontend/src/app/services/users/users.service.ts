import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { map } from 'rxjs/operators';
import { API_URL } from 'src/app/app.constants';
import JsonMessage from 'src/app/models/JsonMessage.models';
import UserRequestDto from 'src/app/models/user/userRequestDto.model';
import UserResponseDto from 'src/app/models/user/userResponseDto.model';
import UserWithJwtResponseDto from 'src/app/models/user/userWithJwtResponseDto.model';
import { AuthenticationService } from '../auth/authentication.service';

@Injectable({
  providedIn: 'root'
})
export class UsersService {
  private readonly usersUrl = API_URL + '/users';

  // Observable string source
  private reservationCountSource = new Subject<number>();
  // Observable string stream
  reservationCountUpdated$ = this.reservationCountSource.asObservable();

  constructor(private httpClient: HttpClient, private authService: AuthenticationService) { }

  updateReservationCount() {
    this.getUserReservationCount(this.authService.getLoggedInUserId()).subscribe(
      response => {
        this.reservationCountSource.next(response)
      }
    )
  }

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

  getUserReservationCount(id) {
    return this.httpClient.get<number>(`${this.usersUrl}/${id}/reservationCount`);
  }

  deleteUserById(id: number) {
    return this.httpClient.delete<JsonMessage>(`${this.usersUrl}/${id}`);
  }
}
