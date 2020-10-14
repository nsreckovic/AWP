import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { GroupService } from 'src/app/service/groups/group.service';

@Component({
  selector: 'app-groups',
  templateUrl: './groups.component.html',
  styleUrls: ['./groups.component.css'],
})
export class GroupsComponent implements OnInit {
  success: boolean;
  message: string;

  constructor(public groupService: GroupService, private router: Router) {}

  ngOnInit(): void {}

  groupDetails(group) {
    this.router.navigate(['groups', group.name, 'details']);
  }

  addGroup() {
    this.router.navigate(['groups', 'new']);
  }

  editGroup(group) {
    this.router.navigate(['groups', group.name]);
  }

  deleteGroup(group) {
    if (this.groupService.remove(group)) {
      this.success = true;
      this.message = 'Group deleted successfully.';
    } else {
      this.success = false;
      this.message = 'Error occured while deleting the group.';
    }
  }
}
