import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { API_URL } from 'src/app/app.constants';
import { map } from 'rxjs/operators';
import UserWithJwtResponseDto from 'src/app/models/user/userWithJwtResponseDto.model';

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {
  constructor(private httpClient: HttpClient) {}

  authenticate(username, password) {
    return this.httpClient
      .get<UserWithJwtResponseDto>(`${API_URL}/auth/generateToken`, {
        params: {
          username,
          password,
        },
      })
      .pipe(
        map((response) => {
          localStorage.setItem('username', response.user.username);
          localStorage.setItem('userId', response.user.id.toString());
          localStorage.setItem('jwt', response.jwt);
        })
      );
  }

  isUserLoggedIn() {
    let user = localStorage.getItem('userId');
    let jwt = localStorage.getItem('jwt');
    return user !== null && jwt !== null;
  }

  getLoggedInUsername() {
    return localStorage.getItem('username');
  }

  getLoggedInUserId() {
    return localStorage.getItem('userId');
  }

  getLoggedInUserJwt() {
    return localStorage.getItem('jwt');
  }

  logout() {
    localStorage.removeItem('username');
    localStorage.removeItem('userId');
    localStorage.removeItem('jwt');
  }
}
