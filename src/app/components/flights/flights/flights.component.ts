import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import Flight from 'src/app/models/flight/flight.model';
import { AuthenticationService } from 'src/app/services/auth/authentication.service';
import { FlightsService } from 'src/app/services/flights/flights.service';
declare var $: any;

@Component({
  selector: 'app-flights',
  templateUrl: './flights.component.html',
  styleUrls: ['./flights.component.css']
})
export class FlightsComponent implements OnInit {
  flights: Flight[];
  errorMessage: string = null;
  successMessage: string = null;
  private flightForDelete = null;

  constructor(
    private authService: AuthenticationService,
    private router: Router,
    private flightsService: FlightsService
  ) {}

  ngOnInit(): void {
    if (!this.authService.isAdminLoggedIn()) this.router.navigate(['/']);
    this.getFlights();
  }

  getFlights() {
    this.flights = [];
    this.flightsService.getAllFlights().subscribe(
      (response) => {
        this.flights = response;
      },
      (error) => {
        this.errorMessage = error.error;
      }
    );
  }

  newFlight() {
    this.router.navigate(['flights', 'new']);
  }

  editFlight(flight) {
    this.router.navigate(['flights', flight.id, 'edit']);
  }

  deleteFlight(flight) {
    $('#deleteFlightModal').modal('show');
    this.flightForDelete = flight;
  }

  deleteFlightFinally() {
    this.flightsService.deleteFlightById(this.flightForDelete.id).subscribe(
      (response) => {
        $('#deleteFlightModal').modal('hide');
        this.errorMessage = null;
        this.successMessage = response.message;
        this.getFlights();
      },
      (error) => {
        $('#deleteFlightModal').modal('hide');
        this.successMessage = null;
        this.errorMessage = error.error;
      }
    );
  }

  hideModal() {
    this.flightForDelete = null;
    $('#deleteFlightModal').modal('hide');
  }

  public dismissErrorAlert() {
    this.errorMessage = null;
  }

  public dismissSuccessAlert() {
    this.successMessage = null;
  }
}
