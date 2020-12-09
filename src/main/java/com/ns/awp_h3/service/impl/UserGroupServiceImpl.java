package com.ns.awp_h3.service.impl;

import com.ns.awp_h3.models.UserGroup;
import com.ns.awp_h3.repository.UserGroupRepository;
import com.ns.awp_h3.service.UserGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserGroupServiceImpl implements UserGroupService {
    private final UserGroupRepository userGroupRepository;

    @Override
    public ResponseEntity<?> newUserGroup(UserGroup userGroup) {
        try {
            // Saving
            userGroupRepository.save(userGroup);

            return ResponseEntity.ok(userGroup);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(400).body("Student Group name must be non null and unique.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    @Override
    public ResponseEntity<?> updateUserGroup(UserGroup userGroup) {
        try {
            // Id check
            if (!userGroupRepository.existsById(userGroup.getId())) {
                return ResponseEntity.status(404).body("Student Group with provided id not found.");
            }
            UserGroup existingUserGroup = userGroupRepository.findById(userGroup.getId()).get();

            // New Name check
            if (userGroupRepository.existsByName(userGroup.getName())) return ResponseEntity.status(400).body("Student Group with provided name already exists.");

            // Updating
            existingUserGroup.setName(userGroup.getName());
            userGroupRepository.save(existingUserGroup);

            return ResponseEntity.ok(userGroup);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    @Override
    public ResponseEntity<?> getAllUserGroups() {
        try {
            Iterable<UserGroup> userGroups = userGroupRepository.findAll();
            return ResponseEntity.ok(userGroups);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    @Override
    public ResponseEntity<?> getUserGroupById(int id) {
        try {
            UserGroup userGroup = userGroupRepository.findById(id).get();
            return ResponseEntity.ok(userGroup);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Student Group with provided id not found.");
        }
    }

    @Override
    public ResponseEntity<?> deleteUserGroup(int id) {
        try {
            // Id check
            if (!userGroupRepository.existsById(id)) {
                return ResponseEntity.status(404).body("Student Group with provided id not found.");
            }

            // Deleting
            userGroupRepository.deleteById(id);

            return ResponseEntity.ok("Student Group deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }
}
