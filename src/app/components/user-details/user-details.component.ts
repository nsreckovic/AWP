import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Group } from 'src/app/models/group.model';
import { User } from 'src/app/models/user.model';
import { GroupService } from 'src/app/service/groups/group.service';
import { UserService } from 'src/app/service/users/user.service';

@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html',
  styleUrls: ['./user-details.component.css'],
})
export class UserDetailsComponent implements OnInit {
  user = new User(-1, '', '');
  user_id: number;
  currentGroup: Group;
  currentGroupName: string;
  selectedGroup: Group;
  message = null;
  success: boolean;

  constructor(
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router,
    public groupService: GroupService
  ) {}

  ngOnInit(): void {
    this.user_id = this.route.snapshot.params['id'];
    this.userService.getUser(this.user_id).subscribe((data) => {
      if (data === undefined) this.router.navigate(['users']);
      else {
        this.user = data;
        this.currentGroup = this.groupService.getGroupForUser(this.user);
        if (this.currentGroup === null || this.currentGroup === undefined)
          this.currentGroupName = 'Not in group.';
        else this.currentGroupName = this.currentGroup.name;
      }
    });
  }

  closeAlert() {
    this.message = null;
  }

  saveGroup() {
    if (this.groupService.addUserToGroup(this.selectedGroup, this.user)) {
      this.success = true;
      this.message = 'Group changed successfully.';
      this.currentGroup = this.groupService.getGroupForUser(this.user);
      this.currentGroupName = this.currentGroup.name;
    } else {
      this.success = false;
      this.message = 'Error occured while changing the group.';
    }
  }

  editUser() {

  }

  deleteUser() {
    this.userService.deleteUser(this.user_id).subscribe(
      (response) => {
        this.groupService.removeUserFromGroup(this.user)
        this.router.navigate(['users']);
      },
      (error) => {
        this.message = 'Error occured while deleting the user.'
        this.success = false
      }
    );
  }
}
