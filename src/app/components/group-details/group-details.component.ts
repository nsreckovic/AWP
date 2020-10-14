import { AfterContentInit, Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from 'src/app/models/user.model';
import { GroupService } from 'src/app/service/groups/group.service';
import { UserService } from 'src/app/service/users/user.service';

@Component({
  selector: 'app-group-details',
  templateUrl: './group-details.component.html',
  styleUrls: ['./group-details.component.css'],
})
export class GroupDetailsComponent implements OnInit {
  message = null;
  success: boolean;
  groupName: string;
  users: User[];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private groupService: GroupService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.groupName = this.route.snapshot.params['name'];
    this.getUsers();
  }

  getUsers() {
    this.users = this.groupService.getAllUsersForGroup(this.groupName);
    // if (this.users == null) {
    //   this.success = false;
    //   this.message = 'This group has no users.';
    // }
  }

  closeAlert() {
    this.message = undefined;
  }

  userDetails(user) {
    this.router.navigate(['users', user.id, 'details']);
  }

  addUser() {
    this.router.navigate(['users', -1]);
  }

  editUser(user) {
    this.router.navigate(['users', user.id]);
  }

  deleteUser(user) {
    this.userService.deleteUser(user.id).subscribe(
      (response) => {
        this.groupService.removeUserFromGroup(user);
        this.success = true;
        this.message = 'User deleted successfully.';
        this.getUsers();
      },
      (error) => {
        console.log(error);
      }
    );
  }
}
