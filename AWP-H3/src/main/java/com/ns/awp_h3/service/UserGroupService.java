package com.ns.awp_h3.service;

import com.ns.awp_h3.models.UserGroup;
import org.springframework.http.ResponseEntity;

public interface UserGroupService {
    ResponseEntity<?> newUserGroup(UserGroup userGroup);

    ResponseEntity<?> updateUserGroup(UserGroup userGroup);

    ResponseEntity<?> getAllUserGroups();

    ResponseEntity<?> getUserGroupById(int id);

    ResponseEntity<?> deleteUserGroup(int id);
}
