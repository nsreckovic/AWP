import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import UserRequestDto from 'src/app/models/user/userRequestDto.model';
import UserType from 'src/app/models/userType/userType.model';
import { AuthenticationService } from 'src/app/services/auth/authentication.service';
import { UsersService } from 'src/app/services/users/users.service';
import { UserTypesService } from 'src/app/services/userTypes/user-types.service';

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
  styleUrls: ['./edit-user.component.css'],
})
export class EditUserComponent implements OnInit {
  public userForm: FormGroup;
  errorMessage: string = null;
  user = new UserRequestDto(-1, null, null, null, null, null, null);
  userTypes: UserType[];

  constructor(
    private usersService: UsersService,
    private userTypesService: UserTypesService,
    public authService: AuthenticationService,
    private router: Router,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder
  ) {}

  ngOnInit(): void {
    if (!this.authService.isUserLoggedIn()) this.router.navigate(['/login']);

    if (this.route.snapshot.params['id'] === undefined)
      this.router.navigate(['/users']);
    else this.user.id = this.route.snapshot.params['id'];

    if (this.authService.isAdminLoggedIn()) this.getUserTypes();
    this.getUser();

    if (this.authService.isAdminLoggedIn()) {
      this.userForm = this.formBuilder.group({
        username: ['', [Validators.minLength(2), Validators.required]],
        name: ['', [Validators.minLength(2), Validators.required]],
        lastName: ['', [Validators.minLength(2), Validators.required]],
        userType: ['', [Validators.required]],
      });

    } else {
      this.userForm = this.formBuilder.group({
        username: ['', [Validators.minLength(2), Validators.required]],
        name: ['', [Validators.minLength(2), Validators.required]],
        lastName: ['', [Validators.minLength(2), Validators.required]],
        password: ['', [Validators.minLength(6), Validators.pattern('.*[0-9].*'), Validators.pattern('.*[A-Za-z].*')]],
        newPassword: ['', [Validators.minLength(6), Validators.pattern('.*[0-9].*'), Validators.pattern('.*[A-Za-z].*')]],
      });
    }
  }

  private getUserTypes() {
    this.userTypesService.getAllUserTypes().subscribe(
      (response) => {
        this.userTypes = response;
      },
      (error) => {
        this.errorMessage = error.error;
      }
    );
  }

  private getUser() {
    this.usersService.getUserById(this.user.id).subscribe(
      (response) => {
        this.user.id = response.id;
        this.user.username = response.username;
        this.user.name = response.name;
        this.user.lastName = response.lastName;

        if (this.authService.isAdminLoggedIn()) {
          this.user.userType = response.userType.name;
          this.userForm.setValue({
            username: this.user.username,
            name: this.user.name,
            lastName: this.user.lastName,
            userType: this.user.userType,
          });

        } else {
          this.userForm.setValue({
            username: this.user.username,
            name: this.user.name,
            lastName: this.user.lastName,
            userType: this.user.userType,
            password: null,
            newPassword: null,
          });
        }
          
        
      },
      (error) => {
        this.errorMessage = error.error;
      }
    );
  }

  public get username() {
    return this.userForm.get('username');
  }

  public get name() {
    return this.userForm.get('name');
  }

  public get lastName() {
    return this.userForm.get('lastName');
  }

  public get userType() {
    return this.userForm.get('userType');
  }

  public get password() {
    return this.userForm.get('password');
  }

  public get newPassword() {
    return this.userForm.get('newPassword');
  }

  editUser(userForm) {
    this.user.username = userForm.username,
    this.user.name = userForm.name,
    this.user.lastName = userForm.lastName,
    this.user.userType = userForm.userType,
    this.user.password = userForm.password,
    this.user.newPassword = userForm.newPassword,

    this.usersService.updateUser(this.user).subscribe(
      (response) => {
        this.router.navigate(['users']);
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
