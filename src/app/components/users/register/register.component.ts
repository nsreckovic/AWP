import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import UserRequestDto from 'src/app/models/user/userRequestDto.model';
import UserResponseDto from 'src/app/models/user/userResponseDto.model';
import { AuthenticationService } from 'src/app/services/auth/authentication.service';
import { UsersService } from 'src/app/services/users/users.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent implements OnInit {
  registerForm: FormGroup;
  errorMessage: string = null;

  constructor(
    private usersService: UsersService,
    public authService: AuthenticationService,
    private router: Router,
    private formBuilder: FormBuilder
  ) {}

  ngOnInit(): void {
    if (this.authService.isUserLoggedIn()) {
      if (!this.authService.isAdminLoggedIn()) this.router.navigate(['/']);
    }

    this.registerForm = this.formBuilder.group({
      username: ['', [Validators.required, Validators.minLength(2)]],
      name: ['', [Validators.required, Validators.minLength(2)]],
      lastName: ['', [Validators.required, Validators.minLength(2)]],
      password: ['', [Validators.required, Validators.minLength(6), Validators.pattern('.*[0-9].*'), Validators.pattern('.*[A-Za-z].*')]],
    });
  }

  public get username() {
    return this.registerForm.get('username');
  }

  public get name() {
    return this.registerForm.get('name');
  }

  public get lastName() {
    return this.registerForm.get('lastName');
  }

  public get password() {
    return this.registerForm.get('password');
  }

  registerUser(formData) {
    this.usersService.newUser(formData).subscribe(
      (data) => {
        this.router.navigate(['/login']);
      },
      (error) => {
        this.errorMessage = error.error;
      }
    );
  }

  public dismissError() {
    this.errorMessage = null;
  }
}
