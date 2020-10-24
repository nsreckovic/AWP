import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/service/auth/authentication.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  public loginForm: FormGroup;
  error_message = 'Invalid credentials. Please try again.';
  error = false;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private authService: AuthenticationService
  ) {
    this.loginForm = this.formBuilder.group({
      username: ['', [Validators.required, Validators.minLength(4)]],
      password: ['', [Validators.required, Validators.minLength(4)]],
      jwtDuration: ['', [Validators.required, Validators.pattern("^-?[0-9]*$"), Validators.min(1)]],
    });
  }

  ngOnInit(): void {}

  public get username() {
    return this.loginForm.get('username');
  }

  public get password() {
    return this.loginForm.get('password');
  }

  public get jwtDuration() {
    return this.loginForm.get('jwtDuration');
  }

  public submitForm(formData) {
    this.authService
      .authenticate(formData.username, formData.password, formData.jwtDuration)
      .subscribe(
        (data) => {
          this.router.navigate(['/users']);
        },
        (error) => {
          this.error = true;
        }
      );
  }
}
