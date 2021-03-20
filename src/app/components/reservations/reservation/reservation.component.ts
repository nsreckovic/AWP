import { Location } from '@angular/common';
import { Component, ElementRef, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import Airline from 'src/app/models/airline/airline.model';
import Airport from 'src/app/models/airport/airport.model';
import ReservationRequestDto from 'src/app/models/reservation/reservationRequestDto.model';
import TicketFilter from 'src/app/models/ticket/ticketFilter.model';
import TicketWithoutUserResponseDto from 'src/app/models/ticket/ticketWithoutUserResponseDto.model copy';
import UserResponseDto from 'src/app/models/user/userResponseDto.model';
import { AirlinesService } from 'src/app/services/airlines/airlines.service';
import { AirportsService } from 'src/app/services/airports/airports.service';
import { AuthenticationService } from 'src/app/services/auth/authentication.service';
import { ReservationsService } from 'src/app/services/reservations/reservations.service';
import { TicketsService } from 'src/app/services/tickets/tickets.service';
import { UsersService } from 'src/app/services/users/users.service';

@Component({
  selector: 'app-reservation',
  templateUrl: './reservation.component.html',
  styleUrls: ['./reservation.component.css'],
})
export class ReservationComponent implements OnInit {
  errorMessage: string = null;
  editReservation = false;
  isCollapsed = true;
  reservationForm: FormGroup;
  reservation = new ReservationRequestDto(-1, null, null, null);
  oldDepartureTicket: TicketWithoutUserResponseDto;
  oldReturnTicket: TicketWithoutUserResponseDto;
  oldUser: UserResponseDto;
  airports: Airport[];
  airlines: Airline[];
  users: UserResponseDto[];
  fromTickets: TicketWithoutUserResponseDto[];
  returnTickets: TicketWithoutUserResponseDto[];
  filter: TicketFilter = new TicketFilter(
    null,
    null,
    null,
    null,
    null,
    null,
    null
  );

  constructor(
    public airportsService: AirportsService,
    public airlinesService: AirlinesService,
    public reservationsService: ReservationsService,
    public ticketsService: TicketsService,
    public usersService: UsersService,
    public authService: AuthenticationService,
    private router: Router,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private location: Location
  ) {
    
  }

  ngOnInit(): void {
    if (!this.authService.isUserLoggedIn()) this.router.navigate(['/']);
    this.buildForm();
    this.initData();
  }

  buildForm() {
    this.reservationForm = this.formBuilder.group({
      departureTicketId: [null, [Validators.required]],
      returnTicketId: [null, []],
      userId: [null, [Validators.required]],
    });
  }

  initData() {
    this.getAirports();
    this.getAirlines();

    if (this.authService.isAdminLoggedIn()) this.getUsers();
    else this.userId.setValue(this.authService.getLoggedInUserId());

    if (this.route.snapshot.params['operation'] === 'edit') {
      if (this.route.snapshot.params['id'] == null)
        this.router.navigate(['/reservations']);
      else this.reservation.id = this.route.snapshot.params['id'];
      this.editReservation = true;
      this.getReservation();
    } else if (this.route.snapshot.params['operation'] === 'new') {
    } else {
      this.router.navigate(['/reservations']);
    }

    if (!this.editReservation) this.getFromTickets();
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

  getReservation() {
    this.reservationsService.getReservationById(this.reservation.id).subscribe(
      (response) => {
        this.oldDepartureTicket = new TicketWithoutUserResponseDto(
          response.departureTicket.id,
          response.departureTicket.flightInstance
        );
        

        if (response.returnTicket) {
          this.oldReturnTicket = new TicketWithoutUserResponseDto(
            response.returnTicket.id,
            response.returnTicket.flightInstance
          );
        }

        this.oldUser = response.user;
        this.userId.setValue(this.oldUser.id);

        this.getFromTickets()
      },
      (error) => {
        this.errorMessage = error.error;
      }
    );
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
      if (dateToValidate == null) return null;
      var date = new Date();
      date.setFullYear(dateToValidate.year);
      date.setDate(dateToValidate.day);
      date.setMonth(dateToValidate.month - 1);
      date.setHours(0);
      date.setMinutes(0);
      date.setSeconds(0);
      return date.getTime();
    }
    return null;
  }

  toDateInMillis(dateToValidate): number {
    if (this.dateValidator(dateToValidate)) {
      if (dateToValidate == null) return null;
      var date = new Date();
      date.setFullYear(dateToValidate.year);
      date.setDate(dateToValidate.day);
      date.setMonth(dateToValidate.month - 1);
      date.setHours(23);
      date.setMinutes(59);
      date.setSeconds(59);
      return date.getTime();
    }
    return null;
  }

  getFromTickets() {
    this.fromTickets = [];
    this.returnTickets = [];

    if (this.editReservation) {
      this.fromTickets.push(this.oldDepartureTicket);
      this.departureTicketId.setValue(this.oldDepartureTicket.id);
      if (this.oldReturnTicket) {
        this.returnTickets.push(this.oldReturnTicket);
        this.returnTicketId.setValue(this.oldReturnTicket.id);
      }
      
    } else {
      this.departureTicketId.setValue(null);
      this.returnTicketId.setValue(null);
    }

    var from = this.filter.fromDate;
    var to = this.filter.toDate;
    this.filter.fromDate = this.fromDateInMillis(this.filter.fromDate);
    this.filter.toDate = this.toDateInMillis(this.filter.toDate);

    this.ticketsService
      .getAllAvailableFromTicketsByFilter(this.filter)
      .subscribe(
        (response) => {
          this.filter.fromDate = from;
          this.filter.toDate = to;
          response.forEach(r => {
            if (this.editReservation && r.flightInstance.id == this.oldDepartureTicket.flightInstance.id) {
              return;            
            } else {
              this.fromTickets.push(r);
            }
          })
          if (this.editReservation) this.getReturnTickets();
        },
        (error) => {
          this.filter.fromDate = from;
          this.filter.toDate = to;
          this.errorMessage = error.error;
        }
      );
  }

  getReturnTickets() {
    this.returnTicketId.setValue(null);
    if (this.departureTicketId.value != null) {
      var from = this.filter.fromDate;
      var to = this.filter.toDate;
      this.filter.fromDate = this.fromDateInMillis(this.filter.fromDate);
      this.filter.toDate = this.toDateInMillis(this.filter.toDate);
      this.filter.fromTicketId = this.departureTicketId.value;
      this.returnTickets = [];

      if (this.oldReturnTicket) {
        this.returnTickets.push(this.oldReturnTicket);
        this.returnTicketId.setValue(this.oldReturnTicket.id);
      }

      this.ticketsService
        .getAllAvailableReturnTicketsByFilter(this.filter)
        .subscribe(
          (response) => {
            this.filter.fromDate = from;
            this.filter.toDate = to;
            response.forEach(r => {
              if (this.editReservation && this.oldReturnTicket && r.flightInstance.id == this.oldReturnTicket.flightInstance.id) {
                return;            
              } else {
                this.returnTickets.push(r);
              }
            })
          },
          (error) => {
            this.filter.fromDate = from;
            this.filter.toDate = to;
            this.errorMessage = error.error;
          }
        );
    }
  }
  
  saveReservation(newReservationForm) {
    this.reservation.departureTicketId = newReservationForm.departureTicketId;
    this.reservation.returnTicketId = newReservationForm.returnTicketId;
    this.reservation.userId = newReservationForm.userId;

    if (!this.editReservation) {
      this.reservationsService.newReservation(this.reservation).subscribe(
        (response) => {
          if (!this.authService.isAdminLoggedIn()) this.usersService.updateReservationCount();
          this.router.navigate(['reservations']);
        },
        (error) => {
          this.errorMessage = error.error;
        }
      );
    } else {
      if (this.reservation.departureTicketId == this.oldDepartureTicket.id) this.reservation.departureTicketId = null
      if (this.oldReturnTicket && this.reservation.returnTicketId == this.oldReturnTicket.id) this.reservation.returnTicketId = null
      if (this.reservation.userId == this.oldUser.id) this.reservation.userId = null

      this.reservationsService.updateReservation(this.reservation).subscribe(
        (response) => {
          this.router.navigate(['reservations']);
        },
        (error) => {
          this.errorMessage = error.error;
        }
      );
    }

    

    
  }

  public dismissError() {
    this.errorMessage = null;
  }

  cancel() {
    this.location.back();
  }

  resetFilter() {
    this.filter = {
      userId: null,
      fromDate: null,
      toDate: null,
      fromAirportId: null,
      toAirportId: null,
      airlineId: null,
      fromTicketId: null,
    };
    this.getFromTickets();
    //this.getReturnTickets();
  }

  public get departureTicketId() {
    return this.reservationForm.get('departureTicketId');
  }

  public get returnTicketId() {
    return this.reservationForm.get('returnTicketId');
  }

  public get userId() {
    return this.reservationForm.get('userId');
  }
}
