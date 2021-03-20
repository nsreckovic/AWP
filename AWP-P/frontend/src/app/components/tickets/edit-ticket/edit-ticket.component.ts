import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import FlightInstanceResponseDto from 'src/app/models/flightInstance/flightInstanceResponseDto.model';
import TicketRequestDto from 'src/app/models/ticket/ticketRequestDto.model';
import UserResponseDto from 'src/app/models/user/userResponseDto.model';
import { AuthenticationService } from 'src/app/services/auth/authentication.service';
import { FlightInstancesService } from 'src/app/services/flightInstances/flight-instances.service';
import { TicketsService } from 'src/app/services/tickets/tickets.service';
import { UsersService } from 'src/app/services/users/users.service';

@Component({
  selector: 'app-edit-ticket',
  templateUrl: './edit-ticket.component.html',
  styleUrls: ['./edit-ticket.component.css']
})
export class EditTicketComponent implements OnInit {
  editTicket = false;
  errorMessage: string = null;
  ticketForm: FormGroup;
  ticket = new TicketRequestDto(-1, null, null);
  flightInstances: FlightInstanceResponseDto[];
  users: UserResponseDto[];

  constructor(
    public ticketsService: TicketsService,
    public flightInstancesService: FlightInstancesService,
    public usersService: UsersService,
    public authService: AuthenticationService,
    private router: Router,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private location: Location
  ) {}

  ngOnInit(): void {
    if (!this.authService.isAdminLoggedIn()) this.router.navigate(['/']);
    this.initData();
    this.buildForm();
  }

  initData() {
    if (this.route.snapshot.params['id'] == null)
      this.router.navigate(['/tickets']);
    else this.ticket.id = this.route.snapshot.params['id'];
    this.getFlightInstances();
    this.getTicket();
  }

  getTicket() {
    this.ticketsService.getTicketById(this.ticket.id).subscribe(
      (response) => {
        this.ticket.flightInstanceId = response.flightInstance.id;
        this.ticketForm.setValue({
          flightInstanceId: this.ticket.flightInstanceId
        });
      },
      (error) => {
        this.errorMessage = error.error;
      }
    );
  }

  getFlightInstances() {
    this.flightInstancesService.getAllFlightInstances().subscribe(
      (response) => {
        this.flightInstances = response;
      },
      (error) => {
        this.errorMessage = error.error;
      }
    );
  }

  buildForm() {
    this.ticketForm = this.formBuilder.group(
      {
        flightInstanceId: [null, [Validators.required]]
      }
    );
  }

  saveTicket(ticketForm) {
    this.ticket.flightInstanceId = ticketForm.flightInstanceId;

    this.ticketsService.updateTicket(this.ticket).subscribe(
      (response) => {
        this.router.navigate(['tickets']);
      },
      (error) => {
        this.errorMessage = error.error;
      }
    );
  }

  public dismissError() {
    this.errorMessage = null;
  }

  cancel() {
    this.location.back();
  }

  public get flightInstanceId() {
    return this.ticketForm.get('flightInstanceId');
  }
}
