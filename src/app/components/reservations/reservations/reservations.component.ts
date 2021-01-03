import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import Airline from 'src/app/models/airline/airline.model';
import Airport from 'src/app/models/airport/airport.model';
import ReservationResponseDto from 'src/app/models/reservation/reservationResponseDto.model';
import UserResponseDto from 'src/app/models/user/userResponseDto.model';
import { AirlinesService } from 'src/app/services/airlines/airlines.service';
import { AirportsService } from 'src/app/services/airports/airports.service';
import { AuthenticationService } from 'src/app/services/auth/authentication.service';
import { ReservationsService } from 'src/app/services/reservations/reservations.service';
import { UsersService } from 'src/app/services/users/users.service';

declare var $: any;

@Component({
  selector: 'app-reservations',
  templateUrl: './reservations.component.html',
  styleUrls: ['./reservations.component.css']
})
export class ReservationsComponent implements OnInit {
  reservations: ReservationResponseDto[];
  errorMessage: string = null;
  successMessage: string = null;
  private reservationForDelete = null;
  filter = {
    userId: null,
    fromDate: null,
    toDate: null,
    fromAirportId: null,
    toAirportId: null,
    airlineId: null
  }
  isCollapsed = true;
  airports: Airport[]
  airlines: Airline[]
  users: UserResponseDto[]

  constructor(
    public authService: AuthenticationService,
    private router: Router,
    private reservationsService: ReservationsService,
    private airlinesService: AirlinesService,
    private airportsService: AirportsService,
    private usersService: UsersService,
  ) {}

  ngOnInit(): void {
    if (!this.authService.isUserLoggedIn()) this.router.navigate(['/login']);
    if (!this.authService.isAdminLoggedIn()) this.filter.userId = this.authService.getLoggedInUserId()
    this.initData();
  }

  initData() {
    this.getReservations();
    this.getAirlines();
    this.getAirports();
    if (this.authService.isAdminLoggedIn()) this.getUsers();
  }

  dateValidator(dateStr): boolean {
    if (dateStr && typeof dateStr === 'string') {
      let match = dateStr.match(
        /^([12]\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01]))/
      );
      if (!match) {
        return false;
      } else if (match && match[0] !== dateStr) {
        return false;
      }
    }
    return true;
  }

  fromDateInMillis(dateToValidate): number {
    if (this.dateValidator(dateToValidate)) {
      if (dateToValidate == null) return null
      var date = new Date()
      date.setFullYear(dateToValidate.year)
      date.setDate(dateToValidate.day)
      date.setMonth(dateToValidate.month - 1)
      date.setHours(0)
      date.setMinutes(0)
      date.setSeconds(0)
      return date.getTime()
    }
    return null
  }

  toDateInMillis(dateToValidate): number {
    if (this.dateValidator(dateToValidate)) {
      if (dateToValidate == null) return null
      var date = new Date()
      date.setFullYear(dateToValidate.year)
      date.setDate(dateToValidate.day)
      date.setMonth(dateToValidate.month - 1)
      date.setHours(23)
      date.setMinutes(59)
      date.setSeconds(59)
      return date.getTime()
    }
    return null
  }

  getReservations() {
    var from = this.filter.fromDate
    var to = this.filter.toDate
    this.filter.fromDate = this.fromDateInMillis(this.filter.fromDate)
    this.filter.toDate = this.toDateInMillis(this.filter.toDate)
    this.reservations = [];
    this.reservationsService.getAllReservations(this.filter).subscribe(
      (response) => {
        this.filter.fromDate = from
        this.filter.toDate = to
        this.reservations = response;
      },
      (error) => {
        this.filter.fromDate = from
        this.filter.toDate = to
        this.errorMessage = error.error;
      }
    );
  }

  getAirports() {
    this.airports = [];
    this.airportsService.getAllAirports().subscribe(
      (response) => {
        this.airports = response;
      },
      (error) => {
        this.errorMessage = error.error;
      }
    );
  } 

  getAirlines() {
    this.airlines = [];
    this.airlinesService.getAllAirlines().subscribe(
      (response) => {
        this.airlines = response;
      },
      (error) => {
        this.errorMessage = error.error;
      }
    );
  }

  getUsers() {
    this.users = [];
    this.usersService.getAllUsers().subscribe(
      (response) => {
        this.users = response;
      },
      (error) => {
        this.errorMessage = error.error;
      }
    );
  }

  newReservation() {
    this.router.navigate(['reservations', 'new']);
  }

  editReservation(reservation) {
    if (!this.authService.isAdminLoggedIn()) this.router.navigate(['reservations'])
    this.router.navigate(['reservations', reservation.id, 'edit']);
  }

  deleteReservation(reservation) {
    if (!this.authService.isAdminLoggedIn()) this.router.navigate(['reservations'])
    $('#deleteReservationModal').modal('show');
    this.reservationForDelete = reservation;
  }

  deleteReservationFinally() {
    this.reservationsService.deleteReservationById(this.reservationForDelete.id).subscribe(
      (response) => {
        $('#deleteReservationModal').modal('hide');
        this.errorMessage = null;
        this.successMessage = response.message;
        this.getReservations();
      },
      (error) => {
        $('#deleteReservationModal').modal('hide');
        this.successMessage = null;
        this.errorMessage = error.error;
      }
    );
  }

  hideModal() {
    this.reservationForDelete = null;
    $('#deleteReservationModal').modal('hide');
  }

  resetFilter() {
    this.filter = {
      userId: null,
      fromDate: null,
      toDate: null,
      fromAirportId: null,
      toAirportId: null,
      airlineId: null
    }
    this.getReservations()
  }

  public dismissErrorAlert() {
    this.errorMessage = null;
  }

  public dismissSuccessAlert() {
    this.successMessage = null;
  }
}
