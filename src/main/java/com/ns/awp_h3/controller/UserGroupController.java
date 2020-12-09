package com.ns.awp_h3.controller;

import com.ns.awp_h3.models.UserGroup;
import com.ns.awp_h3.service.UserGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/userGroups")
@RequiredArgsConstructor
public class UserGroupController {
    private final UserGroupService userGroupService;

    @PostMapping("/")
    public ResponseEntity<?> addNewUserGroup(@RequestBody UserGroup userGroup) {
        return userGroupService.newUserGroup(userGroup);
    }

    @PutMapping("/")
    public ResponseEntity<?> updateUserGroup(@RequestBody UserGroup userGroup) {
        return userGroupService.updateUserGroup(userGroup);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUserGroups() {
        return userGroupService.getAllUserGroups();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserGroupById(@PathVariable("id") int id) {
        return userGroupService.getUserGroupById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserGroup(@PathVariable("id") int id) {
        return userGroupService.deleteUserGroup(id);
    }
}
