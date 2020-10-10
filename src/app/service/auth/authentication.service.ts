import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor() { }

  authenticate(username, password) {
    if (username === 'username' && password === 'password') {
      sessionStorage.setItem('user', username)
      return true
    } 
    return false
  }

  isUserLoggedIn() {
    let user = sessionStorage.getItem('user')
    return user !== null
  }

  logout() {
    sessionStorage.removeItem('user')
  }
  
}
