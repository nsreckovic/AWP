import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import Airline from 'src/app/models/airline/airline.model';
import { AirlinesService } from 'src/app/services/airlines/airlines.service';
import { AuthenticationService } from 'src/app/services/auth/authentication.service';
declare var $: any;

@Component({
  selector: 'app-airlines',
  templateUrl: './airlines.component.html',
  styleUrls: ['./airlines.component.css']
})
export class AirlinesComponent implements OnInit {
  airlines: Airline[];
  errorMessage: string = null;
  successMessage: string = null;
  private airlineForDelete = null;

  constructor(
    private authService: AuthenticationService,
    private router: Router,
    private airlinesService: AirlinesService
  ) {}

  ngOnInit(): void {
    if (!this.authService.isAdminLoggedIn()) this.router.navigate(['/']);
    this.getAirlines();
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

  editAirline(airline) {
    this.router.navigate(['airlines', airline.id, 'edit']);
  }

  deleteAirline(airline) {
    $('#deleteAirlineModal').modal('show');
    this.airlineForDelete = airline;
  }

  deleteAirlineFinally() {
    this.airlinesService.deleteAirlineById(this.airlineForDelete.id).subscribe(
      (response) => {
        $('#deleteAirlineModal').modal('hide');
        this.errorMessage = null;
        this.successMessage = response.message;
        this.getAirlines();
      },
      (error) => {
        $('#deleteAirlineModal').modal('hide');
        this.successMessage = null;
        this.errorMessage = error.error;
      }
    );
  }

  hideModal() {
    this.airlineForDelete = null;
    $('#deleteAirlineModal').modal('hide');
  }

  public dismissErrorAlert() {
    this.errorMessage = null;
  }

  public dismissSuccessAlert() {
    this.successMessage = null;
  }

  newAirline() {
    this.router.navigate(['airlines', 'new']);
  }
}
