import { Injectable } from '@angular/core';
import { User } from './models/user.model';

@Injectable()
export class Data {
  public constructor() {}

  setUser(user: User) {
    localStorage.setItem('userData', JSON.stringify(user));
  }

  getUser(): User {
    return JSON.parse(localStorage.getItem('userData'));
  }

  removeUser() {
    localStorage.removeItem('userData')
  }
}
