import { Injectable } from '@angular/core';
import { Group } from 'src/app/models/group.model';
import { User } from 'src/app/models/user.model';

@Injectable({
  providedIn: 'root',
})
export class GroupService {
  public groups: Group[];

  constructor() {
    this.groups = []
    for (var i = 400; i < 410; i++) {
      this.groups.push(new Group(i.toString(), []))
    }
  }

  getAll() {
    return this.groups;
  }

  getByName(group_name) {
    return this.groups.filter((g) => (g.name = group_name))[0];
  }

  add(group: Group): boolean {
    if (!this.groups.filter((g) => (g.name = group.name))[0]) {
      this.groups.push(group);
      return true;
    }
    return false;
  }

  remove(group: Group): boolean {
    for (var i = 0; i < this.groups.length; i++) {
      if (this.groups[i].name === group.name) {
        this.groups.splice(i, 1);
        return true;
      }
    }
    return false;
  }

  addUserToGroup(group: Group, user: User): boolean {
    var groupExist = this.groups.filter((g) => (g.name = group.name))[0];
    if (groupExist) {
      if (!groupExist.users.some((u) => { return u.id === user.id; })) {
        groupExist.users.push(user);
        return true;
      }
    }
    return false;
  }

  removeUserFromGroup(group: Group, user: User): boolean {
    var groupExist = this.groups.filter(g => g.name = group.name)[0]
    if (groupExist) {
      for (var i = 0; i < groupExist.users.length; i++) {
        if (groupExist.users[i].id === user.id) {
          this.groups.splice(i, 1);
          return true;
        }
      }
    }
    return false;
  }
}
