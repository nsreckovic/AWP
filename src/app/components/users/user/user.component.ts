import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import UserResponseDto from 'src/app/models/user/userResponseDto.model';
import { AuthenticationService } from 'src/app/services/auth/authentication.service';
import { UsersService } from 'src/app/services/users/users.service';
declare var $: any;

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css'],
})
export class UserComponent implements OnInit {
  errorMessage: string;
  user = new UserResponseDto(-1, null, null, null, null);
  userId: number;

  constructor(
    private usersService: UsersService,
    public authService: AuthenticationService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    if (!this.authService.isUserLoggedIn()) this.router.navigate(['/']);

    if (this.route.snapshot.params['id'] === undefined)
      this.router.navigate(['/users']);
    else this.user.id = this.route.snapshot.params['id'];

    this.getUser();
  }

  private getUser() {
    this.usersService.getUserById(this.user.id).subscribe(
      (response) => {
        this.user.id = response.id;
        this.user.username = response.username;
        this.user.name = response.name;
        this.user.lastName = response.lastName;
        this.user.userType = response.userType;
      },
      (error) => {
        if (error.status === 401) this.router.navigate(['/']);
        this.errorMessage = error.error;
      }
    );
  }

  editUser() {
    this.router.navigate(['users', this.user.id, 'edit']);
  }

  deleteUser() {
    $('#deleteUserModal').modal('show');
  }

  deleteUserFinally() {
    this.usersService.deleteUserById(this.user.id).subscribe(
      (response) => {
        $('#deleteUserModal').modal('hide');
        this.authService.logout()
        this.router.navigate(['login']);
      },
      (error) => {
        $('#deleteUserModal').modal('hide');
        this.errorMessage = error.error;
      }
    );
  }

  hideModal() {
    $('#deleteUserModal').modal('hide');
  }

  public dismissError() {
    this.errorMessage = null;
  }
}
