import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import Airport from 'src/app/models/airport/airport.model';
import { AirportsService } from 'src/app/services/airports/airports.service';
import { AuthenticationService } from 'src/app/services/auth/authentication.service';
declare var $: any;

@Component({
  selector: 'app-airports',
  templateUrl: './airports.component.html',
  styleUrls: ['./airports.component.css']
})
export class AirportsComponent implements OnInit {
  airports: Airport[];
  errorMessage: string = null;
  successMessage: string = null;
  private airportForDelete = null;

  constructor(
    private authService: AuthenticationService,
    private router: Router,
    private airportsService: AirportsService
  ) {}

  ngOnInit(): void {
    if (!this.authService.isAdminLoggedIn()) this.router.navigate(['/']);
    this.getAirports();
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

  editAirport(airport) {
    this.router.navigate(['airports', airport.id, 'edit']);
  }

  deleteAirport(airport) {
    $('#deleteAirportModal').modal('show');
    this.airportForDelete = airport;
  }

  deleteAirportFinally() {
    this.airportsService.deleteAirportById(this.airportForDelete.id).subscribe(
      (response) => {
        $('#deleteAirportModal').modal('hide');
        this.errorMessage = null;
        this.successMessage = response.message;
        this.getAirports();
      },
      (error) => {
        $('#deleteAirportModal').modal('hide');
        this.successMessage = null;
        this.errorMessage = error.error;
      }
    );
  }

  newAirport() {
    this.router.navigate(['airports', 'new']);
  }

  hideModal() {
    this.airportForDelete = null;
    $('#deleteAirportModal').modal('hide');
  }

  public dismissErrorAlert() {
    this.errorMessage = null;
  }

  public dismissSuccessAlert() {
    this.successMessage = null;
  }
}
