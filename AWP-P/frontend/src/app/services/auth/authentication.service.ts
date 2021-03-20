import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { API_URL } from 'src/app/app.constants';
import { map } from 'rxjs/operators';
import UserWithJwtResponseDto from 'src/app/models/user/userWithJwtResponseDto.model';
import UserLoginRequestDto from 'src/app/models/user/userLoginRequestDto.model';

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {
  constructor(private httpClient: HttpClient) {}

  authenticate(username, password) {
    return this.httpClient
      .post<UserWithJwtResponseDto>(
        `${API_URL}/auth/generateToken`,
        new UserLoginRequestDto(username, password)
      )
      .pipe(
        map((response) => {
          localStorage.setItem('user', JSON.stringify(response.user));
          localStorage.setItem('jwt', response.jwt);
        })
      );
  }

  isUserLoggedIn() {
    let user = localStorage.getItem('user');
    let jwt = localStorage.getItem('jwt');
    return user !== null && jwt !== null;
  }

  getLoggedInUsername() {
    var user = JSON.parse(localStorage.getItem('user'));
    if (user) return user.username;
  }

  getLoggedInUserId() {
    var user = JSON.parse(localStorage.getItem('user'));
    return user.id;
  }

  isAdminLoggedIn() {
    var user = JSON.parse(localStorage.getItem('user'));
    if (user && user.userType.name === 'ADMIN') return true;
    else return false;
  }

  getLoggedInUserJwt() {
    return localStorage.getItem('jwt');
  }

  replaceUser(response: UserWithJwtResponseDto) {
    localStorage.removeItem('user');
    localStorage.removeItem('jwt');
    
    localStorage.setItem('user', JSON.stringify(response.user));
    localStorage.setItem('jwt', response.jwt);
  }

  logout() {
    localStorage.removeItem('user');
    localStorage.removeItem('jwt');
  }
}
