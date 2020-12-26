import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbCalendar } from '@ng-bootstrap/ng-bootstrap';
import Flight from 'src/app/models/flight/flight.model';
import FlightInstanceRequestDto from 'src/app/models/flightInstance/flightInstanceRequestDto.model';
import { AuthenticationService } from 'src/app/services/auth/authentication.service';
import { FlightInstancesService } from 'src/app/services/flightInstances/flight-instances.service';
import { FlightsService } from 'src/app/services/flights/flights.service';

@Component({
  selector: 'app-flight-instance',
  templateUrl: './flight-instance.component.html',
  styleUrls: ['./flight-instance.component.css'],
})
export class FlightInstanceComponent implements OnInit {
  editFlightInstance = false;
  errorMessage: string = null;
  flightInstanceForm: FormGroup;
  flightInstance = new FlightInstanceRequestDto(-1, null, null, null, null);
  flights: Flight[];

  constructor(
    public flightInstancesService: FlightInstancesService,
    public flightsService: FlightsService,
    public authService: AuthenticationService,
    private router: Router,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private location: Location,
    private calendar: NgbCalendar
  ) {}

  ngOnInit(): void {
    if (!this.authService.isAdminLoggedIn()) this.router.navigate(['/']);
    this.initData();
    this.buildForm();
  }

  initData() {
    this.getFlights();

    if (this.route.snapshot.params['operation'] === 'edit') {
      if (this.route.snapshot.params['id'] == null)
        this.router.navigate(['/flightInstances']);
      else this.flightInstance.id = this.route.snapshot.params['id'];
      this.editFlightInstance = true;
      this.getFlightInstance();
    } else if (this.route.snapshot.params['operation'] === 'new') {
    } else {
      this.router.navigate(['/flightInstances']);
    }
  }

  getFlights() {
    this.flightsService.getAllFlights().subscribe(
      (response) => {
        this.flights = response;
      },
      (error) => {
        this.errorMessage = error.error;
      }
    );
  }

  getFlightInstance() {
    this.flightInstancesService
      .getFlightInstanceById(this.flightInstance.id)
      .subscribe(
        (response) => {
          this.flightInstance.id = response.id;
          this.flightInstance.flightId = response.flight.id;
          this.flightInstance.flightDate = response.flightDate;
          this.flightInstance.flightLengthInMinutes = response.flightLengthInMinutes;
          this.flightInstance.count = response.count;

          var flightDate = new Date(this.flightInstance.flightDate)
          console.log(flightDate)

          this.flightInstanceForm.setValue({
            flightId: this.flightInstance.flightId,
            flightDate: {
              year: flightDate.getFullYear(),
              month: flightDate.getMonth() + 1,
              day: flightDate.getDate(),
            },
            flightTime: {
              hour: flightDate.getHours(),
              minute: flightDate.getMinutes(),
            },
            flightLengthInMinutes: {
              hour: (this.flightInstance.flightLengthInMinutes - this.flightInstance.flightLengthInMinutes % 60) / 60,
              minute: this.flightInstance.flightLengthInMinutes % 60
            },
            count: this.flightInstance.count,
          });

          console.log(this.flightDate.value)
        },
        (error) => {
          this.errorMessage = error.error;
        }
      );
  }

  buildForm() {
    this.flightInstanceForm = this.formBuilder.group({
      flightId: [null, [Validators.required]],
      flightDate: [null, [Validators.required, this.dateValidator]],
      flightTime: [null, [Validators.required]],
      flightLengthInMinutes: [null, [Validators.required, this.lengthValidator]],
      count: [null, [Validators.required, this.countValidator]],
    });
  }

  dateValidator(c: AbstractControl): { [key: string]: boolean } {
    let value = c.value;
    if (value && typeof value === 'string') {
      let match = value.match(
        /^([12]\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01]))/
      );
      if (!match) {
        return { 'dateInvalid': true };
      } else if (match && match[0] !== value) {
        return { 'dateInvalid': true };
      }
    }
    return null;
  }

  lengthValidator(c: AbstractControl): { [key: string]: boolean } {
    let value = c.value;
    if (!value) {
      return null;
    }
    if (value.hour > 18) {
      return { 'lengthInvalid': true };
    }
    return null;
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

  saveFlightInstance(newFlightInstanceForm) {
    this.flightInstance.flightId = newFlightInstanceForm.flightId;
    
    var flightDate = new Date()
    flightDate.setFullYear(newFlightInstanceForm.flightDate.year)
    flightDate.setMonth(newFlightInstanceForm.flightDate.month - 1)
    flightDate.setDate(newFlightInstanceForm.flightDate.day)
    flightDate.setHours(newFlightInstanceForm.flightTime.hour)
    flightDate.setMinutes(newFlightInstanceForm.flightTime.minute)
    flightDate.setSeconds(0)
    this.flightInstance.flightDate = flightDate.getTime();

    this.flightInstance.flightLengthInMinutes = newFlightInstanceForm.flightLengthInMinutes.hour * 60 + newFlightInstanceForm.flightLengthInMinutes.minute;
    this.flightInstance.count = newFlightInstanceForm.count;

    if (this.editFlightInstance) {
      this.flightInstancesService
        .updateFlightInstance(this.flightInstance)
        .subscribe(
          (response) => {
            this.router.navigate(['flightInstances']);
          },
          (error) => {
            this.errorMessage = error.error;
          }
        );
    } else {
      this.flightInstancesService
        .newFlightInstance(this.flightInstance)
        .subscribe(
          (response) => {
            this.router.navigate(['flightInstances']);
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
    return this.flightInstanceForm.get('flightId');
  }

  public get flightDate() {
    return this.flightInstanceForm.get('flightDate');
  }

  public get flightTime() {
    return this.flightInstanceForm.get('flightTime');
  }

  public get flightLengthInMinutes() {
    return this.flightInstanceForm.get('flightLengthInMinutes');
  }

  public get count() {
    return this.flightInstanceForm.get('count');
  }
}
