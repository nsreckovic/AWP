import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import Airline from 'src/app/models/airline/airline.model';
import { AirlinesService } from 'src/app/services/airlines/airlines.service';
import { AuthenticationService } from 'src/app/services/auth/authentication.service';

@Component({
  selector: 'app-airline',
  templateUrl: './airline.component.html',
  styleUrls: ['./airline.component.css'],
})
export class AirlineComponent implements OnInit {
  editAirline = false;
  errorMessage: string = null;
  newAirlineForm: FormGroup;
  airline = new Airline(-1, null, null);

  constructor(
    public airlinesService: AirlinesService,
    public authService: AuthenticationService,
    private router: Router,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private location: Location
  ) {}

  ngOnInit(): void {
    if (!this.authService.isUserLoggedIn()) this.router.navigate(['/login']);

    this.initData();
    this.buildForm();
  }

  initData() {
    if (this.route.snapshot.params['operation'] === 'edit') {
      if (this.route.snapshot.params['id'] == null) this.router.navigate(['/airlines']);
      else this.airline.id = this.route.snapshot.params['id'];
      this.editAirline = true;
      this.getAirline();
    } else if (this.route.snapshot.params['operation'] === 'new') {

    } else {
      this.router.navigate(['/airlines']);
    }
  }

  getAirline() {
    this.airlinesService.getAirlineById(this.airline.id).subscribe(
      (response) => {
        this.airline = response
        this.newAirlineForm.setValue({
          name: this.airline.name,
          link: this.airline.link,
        });
      },
      (error) => {
        this.errorMessage = error.error;
      }
    )
  }

  buildForm() {
    this.newAirlineForm = this.formBuilder.group({
      name: [null, [Validators.required, Validators.minLength(2)]],
      link: [null, [Validators.required, this.linkValidation]],
    });
  }

  linkValidation(control: AbstractControl): { [key: string]: boolean } | null {
    const linkPattern = new RegExp( '^(https?:\\/\\/)?'+ // protocol
                                    '((([a-z\\d]([a-z\\d-]*[a-z\\d])*)\\.)+[a-z]{2,}|'+ // domain name
                                    '((\\d{1,3}\\.){3}\\d{1,3}))'+ // OR ip (v4) address
                                    '(\\:\\d+)?(\\/[-a-z\\d%_.~+]*)*'+ // port and path
                                    '(\\?[;&a-z\\d%_.~+=-]*)?'+ // query string
                                    '(\\#[-a-z\\d_]*)?$','i'); // fragment locator
    if (!linkPattern.test(control.value)) return { 'link': true };
    else return null;
  }

  public get name() {
    return this.newAirlineForm.get('name');
  }

  public get link() {
    return this.newAirlineForm.get('link');
  }

  saveAirline(newAirlineForm) {
    this.airline.name = newAirlineForm.name;
    this.airline.link = newAirlineForm.link;
    
    if (this.editAirline) {
      this.airlinesService.updateAirline(this.airline).subscribe(
        (response) => {
          this.router.navigate(['airlines']);
        },
        (error) => {
          this.errorMessage = error.error;
        }
      );

    } else {
      this.airlinesService.newAirline(this.airline).subscribe(
        (response) => {
          this.router.navigate(['airlines']);
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
}
