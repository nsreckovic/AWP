import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Data } from 'src/app/data';
import { User } from 'src/app/models/user.model';
import { GroupService } from 'src/app/service/groups/group.service';
import { UserService } from 'src/app/service/users/user.service';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css'],
})
export class UsersComponent implements OnInit {
  users: User[];
  success_message: string;

  constructor(
    private userService: UserService,
    private groupService: GroupService,
    private router: Router,
    private data: Data
  ) {}

  ngOnInit(): void {
    this.getUsers();
  }

  getUsers() {
    this.userService.getAllUsers().subscribe(
      (response) => {
        this.users = response;
      },
      (error) => {
        console.log(error);
      }
    );
  }

  userDetails(user) {
    this.data.setUser(user);
    this.router.navigate(['users', user.id, 'details']);
  }

  addUser() {
    this.data.setUser(new User(-1, '', ''));
    this.router.navigate(['users', -1]);
  }

  editUser(user) {
    this.data.setUser(user);
    this.router.navigate(['users', user.id]);
  }

  deleteUser(user) {
    this.userService.deleteUser(user.id).subscribe(
      (response) => {
        this.groupService.removeUserFromGroup(user)
        this.success_message = 'User deleted successfully.';
        this.getUsers();
      },
      (error) => {
        console.log(error);
      }
    );
  }
}
