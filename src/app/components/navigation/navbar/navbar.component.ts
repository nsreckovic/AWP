import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from 'src/app/services/auth/authentication.service';
import { UsersService } from 'src/app/services/users/users.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  reservationCount = 0
  constructor(public authService: AuthenticationService, private usersService: UsersService) {
    usersService.reservationCountUpdated$.subscribe(
      count => { this.reservationCount = count; }
    )
  }

  ngOnInit(): void {
    if (!this.authService.isAdminLoggedIn()) this.usersService.updateReservationCount();
  }

}
