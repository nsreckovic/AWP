import { Component, OnInit } from '@angular/core';
import { GroupService } from 'src/app/service/groups/group.service';

@Component({
  selector: 'app-groups',
  templateUrl: './groups.component.html',
  styleUrls: ['./groups.component.css']
})
export class GroupsComponent implements OnInit {
  success: boolean;
  message: string;

  constructor(public groupService: GroupService) { }

  ngOnInit(): void {
  }

  groupDetails(group) {
  }

  addGroup() {
  }

  editGroup(group) {
  }

  deleteGroup(group) {
    if (this.groupService.remove(group)) {
      this.success = true
      this.message = 'Group deleted successfully.'
    } else {
      this.success = false
      this.message = 'Error occured while deleting the group.'
    } 
  }

}
