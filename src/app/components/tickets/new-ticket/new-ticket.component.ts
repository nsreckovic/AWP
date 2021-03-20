import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import FlightInstanceResponseDto from 'src/app/models/flightInstance/flightInstanceResponseDto.model';
import TicketRequestDto from 'src/app/models/ticket/ticketRequestDto.model';
import { AuthenticationService } from 'src/app/services/auth/authentication.service';
import { FlightInstancesService } from 'src/app/services/flightInstances/flight-instances.service';
import { TicketsService } from 'src/app/services/tickets/tickets.service';

@Component({
  selector: 'app-new-ticket',
  templateUrl: './new-ticket.component.html',
  styleUrls: ['./new-ticket.component.css']
})
export class NewTicketComponent implements OnInit {
  errorMessage: string = null;
  ticketForm: FormGroup;
  ticket = new TicketRequestDto(-1, null, null);
  flightInstances: FlightInstanceResponseDto[];

  constructor(
    public ticketsService: TicketsService,
    public flightInstancesService: FlightInstancesService,
    public authService: AuthenticationService,
    private router: Router,
    private formBuilder: FormBuilder,
    private location: Location
  ) {}

  ngOnInit(): void {
    if (!this.authService.isAdminLoggedIn()) this.router.navigate(['/']);
    this.initData();
    this.buildForm();
  }

  initData() {
    this.getFlightInstances();
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
        flightInstanceId: [null, [Validators.required]],
        ticketCount: [null, [Validators.required, this.countValidator]],
      }
    );
  }

  countValidator(c: AbstractControl): { [key: string]: boolean } {
    let value = c.value;
    if (!value && value != 0) {
      return null;
    }
    if (value < 1) {
      return { 'countInvalid': true };
    }
    return null;
  }

  saveTicket(newTicketForm) {
    this.ticket.flightInstanceId = newTicketForm.flightInstanceId;
    this.ticket.ticketCount = newTicketForm.ticketCount;

    this.ticketsService.newTicket(this.ticket).subscribe(
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

  public get ticketCount() {
    return this.ticketForm.get('ticketCount');
  }
}
