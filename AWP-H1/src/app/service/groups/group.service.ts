import { Injectable } from '@angular/core';
import { Group } from 'src/app/models/group.model';
import { User } from 'src/app/models/user.model';

@Injectable({
  providedIn: 'root',
})
export class GroupService {
  public groups: Group[];

  constructor() {
    this.groups = [];
    for (var i = 400; i < 410; i++) {
      this.groups.push(new Group(i.toString(), []));
    }
  }

  getAll(): Group[] {
    return this.groups;
  }

  getAllUsersForGroup(group_name): User[] {
    for (var i = 0; i < this.groups.length; i++) {
      if (this.groups[i].name === group_name) {
        return this.groups[i].users;
      }
    }
    return null;
  }

  getByName(group_name): Group {
    for (var i = 0; i < this.groups.length; i++) {
      if (this.groups[i].name === group_name) {
        return this.groups[i];
      }
    }
    return null;
  }

  add(group: Group): boolean {
    if (!this.groups.some((g) => { return g.name === group.name; })) {
      this.groups.push(group);
      return true;
    }
    return false;
  }

  edit(group: Group): boolean {
    // TODO
    return false
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
    this.removeUserFromGroup(user);
    for (var i = 0; i < this.groups.length; i++) {
      if (this.groups[i].name === group.name) {
        if (
          !this.groups[i].users.some((u) => {
            return u.id === user.id;
          })
        ) {
          this.groups[i].users.push(user);
          return true;
        }
      }
    }
    return false;
  }

  removeUserFromGroup(user: User): boolean {
    for (var i = 0; i < this.groups.length; i++) {
      for (var j = 0; j < this.groups[i].users.length; j++) {
        if (this.groups[i].users[j].id === user.id) {
          this.groups[i].users.splice(j, 1);
          return true;
        }
      }
    }
    return false;
  }

  getGroupForUser(user: User): Group {
    for (var i = 0; i < this.groups.length; i++) {
      if (
        this.groups[i].users.some((u) => {
          return u.id === user.id;
        })
      ) {
        return this.groups[i];
      }
    }
    return null;
  }
}
