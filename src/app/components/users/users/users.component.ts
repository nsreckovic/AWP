import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import UserResponseDto from 'src/app/models/user/userResponseDto.model';
import { AuthenticationService } from 'src/app/services/auth/authentication.service';
import { UsersService } from 'src/app/services/users/users.service';
declare var $: any;

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css'],
})
export class UsersComponent implements OnInit {
  users: UserResponseDto[];
  errorMessage: string = null;
  successMessage: string = null;
  private userForDelete = null;

  constructor(
    private authService: AuthenticationService,
    private router: Router,
    private usersService: UsersService
  ) {}

  ngOnInit(): void {
    if (!this.authService.isAdminLoggedIn()) this.router.navigate(['/']);
    this.getUsers();
  }

  getUsers() {
    this.users = [];
    this.usersService.getAllUsers().subscribe(
      (response) => {
        this.users = response;
      },
      (error) => {
        if (error.status === 403) this.router.navigate(['/']);
        this.errorMessage = error.error;
      }
    );
  }

  userDetails(user) {
    this.router.navigate(['users', user.id, 'details']);
  }

  editUser(user) {
    this.router.navigate(['users', user.id, 'edit']);
  }

  deleteUser(user) {
    $('#deleteUserModal').modal('show');
    this.userForDelete = user;
  }

  deleteUserFinally() {
    this.usersService.deleteUserById(this.userForDelete.id).subscribe(
      (response) => {
        $('#deleteUserModal').modal('hide');
        this.errorMessage = null;
        this.successMessage = response.message;
        this.getUsers();
      },
      (error) => {
        $('#deleteUserModal').modal('hide');
        this.successMessage = null;
        this.errorMessage = error.error;
      }
    );
  }

  hideModal() {
    this.userForDelete = null;
    $('#deleteUserModal').modal('hide');
  }

  dismissErrorAlert() {
    this.errorMessage = null;
  }

  dismissSuccessAlert() {
    this.successMessage = null;
  }
}
