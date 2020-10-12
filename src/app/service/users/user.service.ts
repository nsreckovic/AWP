import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { API_URL } from 'src/app/app.constants';
import { User } from 'src/app/models/user.model';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private httpClient: HttpClient) {}

  getUser(user_id) {
    // TODO this endpoint should exist on API, but it doesn't. 
    // So, if endpoint is available, replace the code below with:
    // return this.httpClient.get<User>(`${API_URL}/users/{user_id}`);
    return this.httpClient.get<User[]>(`${API_URL}/users`).pipe(
      map((response) => {
        return response.filter((user) => user.id == user_id)[0];
      })
    );
  }

  createUser(user) {
    return this.httpClient.post(`${API_URL}/users`, user);
  }

  updateUser(user) {
    return this.httpClient.put(`${API_URL}/users`, user);
  }

  getAllUsers() {
    return this.httpClient.get<User[]>(`${API_URL}/users`);
  }

  deleteUser(user_id) {
    return this.httpClient.delete(`${API_URL}/users/${user_id}`);
  }
}
