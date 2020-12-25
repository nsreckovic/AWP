import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import Airport from 'src/app/models/airport/airport.model';
import { AirportsService } from 'src/app/services/airports/airports.service';
import { AuthenticationService } from 'src/app/services/auth/authentication.service';

@Component({
  selector: 'app-airport',
  templateUrl: './airport.component.html',
  styleUrls: ['./airport.component.css']
})
export class AirportComponent implements OnInit {
  editAirport = false;
  errorMessage: string = null;
  newAirportForm: FormGroup;
  airport = new Airport(-1, null, null, null);

  constructor(
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
    if (this.route.snapshot.params['operation'] === 'edit') {
      if (this.route.snapshot.params['id'] == null) this.router.navigate(['/airports']);
      else this.airport.id = this.route.snapshot.params['id'];
      this.editAirport = true;
      this.getAirport();
    
    } else if (this.route.snapshot.params['operation'] === 'new') {

    } else {
      this.router.navigate(['/airports']);
    }
  }

  getAirport() {
    this.airportsService.getAirportById(this.airport.id).subscribe(
      (response) => {
        this.airport = response
        this.newAirportForm.setValue({
          airportId: this.airport.airportId,
          name: this.airport.name,
          place: this.airport.place,
        });
      },
      (error) => {
        this.errorMessage = error.error;
      }
    )
  }

  buildForm() {
    this.newAirportForm = this.formBuilder.group({
      airportId: [null, [Validators.required, Validators.minLength(2)]],
      name: [null, [Validators.required, Validators.minLength(2)]],
      place: [null, [Validators.required, Validators.minLength(2)]],
    });
  }

  saveAirport(newAirportForm) {
    this.airport.airportId = newAirportForm.airportId;
    this.airport.name = newAirportForm.name;
    this.airport.place = newAirportForm.place;
    
    if (this.editAirport) {
      this.airportsService.updateAirport(this.airport).subscribe(
        (response) => {
          this.router.navigate(['airports']);
        },
        (error) => {
          this.errorMessage = error.error;
        }
      );

    } else {
      this.airportsService.newAirport(this.airport).subscribe(
        (response) => {
          this.router.navigate(['airports']);
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

  
  public get airportId() {
    return this.newAirportForm.get('airportId');
  }

  public get name() {
    return this.newAirportForm.get('name');
  }

  public get place() {
    return this.newAirportForm.get('place');
  }
}
