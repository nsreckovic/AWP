import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import FlightInstanceResponseDto from 'src/app/models/flightInstance/flightInstanceResponseDto.model';
import { AuthenticationService } from 'src/app/services/auth/authentication.service';
import { FlightInstancesService } from 'src/app/services/flightInstances/flight-instances.service';
declare var $: any;

@Component({
  selector: 'app-flight-instances',
  templateUrl: './flight-instances.component.html',
  styleUrls: ['./flight-instances.component.css'],
})
export class FlightInstancesComponent implements OnInit {
  flightInstances: FlightInstanceResponseDto[];
  errorMessage: string = null;
  successMessage: string = null;
  private flightInstanceForDelete = null;

  constructor(
    private authService: AuthenticationService,
    private router: Router,
    private flightInstancesService: FlightInstancesService
  ) {}

  ngOnInit(): void {
    if (!this.authService.isAdminLoggedIn()) this.router.navigate(['/']);
    this.getFlightInstances();
  }

  getFlightInstances() {
    this.flightInstances = [];
    this.flightInstancesService.getAllFlightInstances().subscribe(
      (response) => {
        this.flightInstances = response;
      },
      (error) => {
        this.errorMessage = error.error;
      }
    );
  }

  newFlightInstance() {
    this.router.navigate(['flightInstances', 'new']);
  }

  editFlightInstance(flightInstance) {
    this.router.navigate(['flightInstances', flightInstance.id, 'edit']);
  }

  deleteFlightInstance(flightInstance) {
    $('#deleteFlightInstanceModal').modal('show');
    this.flightInstanceForDelete = flightInstance;
  }

  deleteFlightInstanceFinally() {
    this.flightInstancesService
      .deleteFlightInstanceById(this.flightInstanceForDelete.id)
      .subscribe(
        (response) => {
          $('#deleteFlightInstanceModal').modal('hide');
          this.errorMessage = null;
          this.successMessage = response.message;
          this.getFlightInstances();
        },
        (error) => {
          $('#deleteFlightInstanceModal').modal('hide');
          this.successMessage = null;
          this.errorMessage = error.error;
        }
      );
  }

  hideModal() {
    this.flightInstanceForDelete = null;
    $('#deleteFlightInstanceModal').modal('hide');
  }

  public dismissError() {
    this.errorMessage = null;
  }

  public dismissSuccess() {
    this.successMessage = null;
  }
}
