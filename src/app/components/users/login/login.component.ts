import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/services/auth/authentication.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  public loginForm: FormGroup;
  errorMessage: string = null;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private authService: AuthenticationService
  ) {
    this.loginForm = this.formBuilder.group({
      username: ['', [Validators.required, Validators.minLength(2)]],
      password: ['', [Validators.required]],
    });
  }

  ngOnInit(): void {
    if (this.authService.isUserLoggedIn()) this.router.navigate(['/']);
  }

  public get username() {
    return this.loginForm.get('username');
  }

  public get password() {
    return this.loginForm.get('password');
  }

  public submitForm(formData) {
    this.authService
      .authenticate(formData.username, formData.password)
      .subscribe(
        (response) => {
          this.router.navigate(['/']);
        },
        (error) => {
          this.errorMessage = error.error;
        }
      );
  }

  public dismissErrorAlert() {
    this.errorMessage = null;
  }
}
