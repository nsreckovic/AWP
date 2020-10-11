import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { API_URL } from 'src/app/app.constants';
import { map } from 'rxjs/operators';
import { LoginResponse } from '../../models/loginResponse.model'

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {
  private readonly loginUrl = 'http://localhost:8080/login';

  constructor(private httpClient: HttpClient) {}

  authenticate(username, password, jwt) {
    return this.httpClient
      .get<LoginResponse>(`${API_URL}/login`, {
        params: {
          username,
          password,
          duration: jwt,
        },
      })
      .pipe(
        map((response) => {
          localStorage.setItem('user', response.username);
          localStorage.setItem('jwt', response.jwt);
        })
      );

  }

  isUserLoggedIn() {
    let user = localStorage.getItem('user');
    let jwt = localStorage.getItem('jwt');
    return user !== null && jwt !== null;
  }

  getLoggedInUser() {
    return localStorage.getItem('user')
  }

  getLoggedInUserJWT() {
    return localStorage.getItem('jwt')
  }

  logout() {
    localStorage.removeItem('user');
    localStorage.removeItem('jwt');
  }
}
