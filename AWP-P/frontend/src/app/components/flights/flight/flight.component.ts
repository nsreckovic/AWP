import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import Airline from 'src/app/models/airline/airline.model';
import Airport from 'src/app/models/airport/airport.model';
import FlightRequestDto from 'src/app/models/flight/flightRequestDto.model';
import { AirlinesService } from 'src/app/services/airlines/airlines.service';
import { AirportsService } from 'src/app/services/airports/airports.service';
import { AuthenticationService } from 'src/app/services/auth/authentication.service';
import { FlightsService } from 'src/app/services/flights/flights.service';

@Component({
  selector: 'app-flight',
  templateUrl: './flight.component.html',
  styleUrls: ['./flight.component.css'],
})
export class FlightComponent implements OnInit {
  editFlight = false;
  errorMessage: string = null;
  flightForm: FormGroup;
  flight = new FlightRequestDto(-1, null, null, null, null);
  airlines: Airline[];
  airports: Airport[];

  constructor(
    public flightsService: FlightsService,
    public airlinesService: AirlinesService,
    public airportsService: AirportsService,
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
    this.getAirlines();
    this.getAirports();

    if (this.route.snapshot.params['operation'] === 'edit') {
      if (this.route.snapshot.params['id'] == null)
        this.router.navigate(['/flights']);
      else this.flight.id = this.route.snapshot.params['id'];
      this.editFlight = true;
      this.getFlight();
    } else if (this.route.snapshot.params['operation'] === 'new') {
    } else {
      this.router.navigate(['/flights']);
    }
  }

  getAirlines() {
    this.airlinesService.getAllAirlines().subscribe(
      (response) => {
        this.airlines = response;
      },
      (error) => {
        this.errorMessage = error.error;
      }
    );
  }

  getAirports() {
    this.airportsService.getAllAirports().subscribe(
      (response) => {
        this.airports = response;
      },
      (error) => {
        this.errorMessage = error.error;
      }
    );
  }

  getFlight() {
    this.flightsService.getFlightById(this.flight.id).subscribe(
      (response) => {
        this.flight.id = response.id;
        this.flight.flightId = response.flightId;
        this.flight.departureAirportId = response.departureAirport.id;
        this.flight.arrivalAirportId = response.arrivalAirport.id;
        this.flight.airlineId = response.airline.id;

        this.flightForm.setValue({
          flightId: this.flight.flightId,
          departureAirportId: this.flight.departureAirportId,
          arrivalAirportId: this.flight.arrivalAirportId,
          airlineId: this.flight.airlineId,
        });
      },
      (error) => {
        this.errorMessage = error.error;
      }
    );
  }

  buildForm() {
    this.flightForm = this.formBuilder.group(
      {
        flightId: [null, [Validators.required, Validators.minLength(2), Validators.pattern('^[a-zA-Z0-9 ]+$')]],
        departureAirportId: [null, [Validators.required, this.departureAirportValidator]],
        arrivalAirportId: [null, [Validators.required, this.arrivalAirportValidator]],
        airlineId: [null, [Validators.required]],
      }
    );
  }

  departureAirportValidator(control: AbstractControl): { [key: string]: boolean } | null {
    if (control.value != null && control.value == control.parent.get('arrivalAirportId').value) {
      return { 'sameAirports': true };
    } else {
      return null;
    }
  }

  arrivalAirportValidator(control: AbstractControl): { [key: string]: boolean } | null {
    if (control.value != null && control.value == control.parent.get('departureAirportId').value) {
      return { 'sameAirports': true };
    } else {
      return null;
    }
  }

  saveFlight(newFlightForm) {
    this.flight.flightId = newFlightForm.flightId;
    this.flight.departureAirportId = newFlightForm.departureAirportId;
    this.flight.arrivalAirportId = newFlightForm.arrivalAirportId;
    this.flight.airlineId = newFlightForm.airlineId;

    if (this.editFlight) {
      this.flightsService.updateFlight(this.flight).subscribe(
        (response) => {
          this.router.navigate(['flights']);
        },
        (error) => {
          this.errorMessage = error.error;
        }
      );
    } else {
      this.flightsService.newFlight(this.flight).subscribe(
        (response) => {
          this.router.navigate(['flights']);
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

  public get flightId() {
    return this.flightForm.get('flightId');
  }

  public get departureAirportId() {
    return this.flightForm.get('departureAirportId');
  }

  public get arrivalAirportId() {
    return this.flightForm.get('arrivalAirportId');
  }

  public get airlineId() {
    return this.flightForm.get('airlineId');
  }
}
