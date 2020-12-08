package com.ns.awp_h3.service.impl;

import com.ns.awp_h3.dto.NewUserRequestDto;
import com.ns.awp_h3.dto.UserResponseDto;
import com.ns.awp_h3.dto.UserSearchRequestDto;
import com.ns.awp_h3.models.UserGroup;
import com.ns.awp_h3.models.User;
import com.ns.awp_h3.models.UserType;
import com.ns.awp_h3.repository.UserGroupRepository;
import com.ns.awp_h3.repository.UserRepository;
import com.ns.awp_h3.repository.UserTypeRepository;
import com.ns.awp_h3.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserTypeRepository userTypeRepository;
    private final UserGroupRepository userGroupRepository;

    @Override
    public ResponseEntity newUser(NewUserRequestDto user) {
        try {
            // Username check
            if (userRepository.existsByUsername(user.getUsername())) {
                return ResponseEntity.status(400).body("User with provided username already exists.");
            }

            // UserType check
            UserType userType;
            if (!userTypeRepository.existsById(user.getUserType())) {
                return ResponseEntity.status(400).body("User Type with provided id not found.");
            } else userType = userTypeRepository.findById(user.getUserType()).get();

            // User Group check
            UserGroup userGroup;
            if (!userGroupRepository.existsById(user.getUserGroup()) && user.getUserGroup() != null) {
                return ResponseEntity.status(400).body("Student Group with provided id not found.");
            } else {
                userGroup = user.getUserGroup() != null ? userGroupRepository.findById(user.getUserGroup()).get() : null;
            }

            // Saving
            User saved = userRepository.save(new User(
                    -1,
                    user.getUsername(),
                    user.getPassword(),
                    user.getName(),
                    user.getLastName(),
                    userType,
                    userGroup
            ));

            return ResponseEntity.ok(new UserResponseDto(saved));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(400).body("Invalid request body.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    @Override
    public ResponseEntity updateUser(NewUserRequestDto user) {
        try {
            // Id check
            if (user.getId() == null || !userRepository.existsById(user.getId())) {
                return ResponseEntity.status(404).body("User with provided id not found.");
            }
            User existingUser = userRepository.findById(user.getId()).get();

            // Password check
            if (!existingUser.getPassword().equals(user.getPassword())) {
                return ResponseEntity.status(401).body("Wrong password. Unauthorized update request.");
            }

            // Username check
            if (userRepository.existsByUsername(user.getUsername()) && !existingUser.getUsername().equals(user.getUsername())) {
                return ResponseEntity.status(400).body("User with provided username already exists.");
            } else existingUser.setUsername(user.getUsername());

            // UserType check
            if (!userTypeRepository.existsById(user.getUserType())) {
                return ResponseEntity.status(400).body("User Type with provided id not found.");
            } else existingUser.setUserType(userTypeRepository.findById(user.getUserType()).get());

            // User Group check
            if (user.getUserGroup() != null && !userGroupRepository.existsById(user.getUserGroup())) {
                return ResponseEntity.status(400).body("Student Group with provided id not found.");
            } else {
                existingUser.setUserGroup(user.getUserGroup() != null ? userGroupRepository.findById(user.getUserGroup()).get() : null);
            }

            // New Password check
            if (user.getNewPassword() != null) {
                existingUser.setPassword(user.getNewPassword());
            }

            // Updating
            userRepository.save(existingUser);

            return ResponseEntity.ok(new UserResponseDto(existingUser));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    @Override
    public ResponseEntity getAllUsers() {
        try {
            ArrayList<UserResponseDto> users = new ArrayList<>();
            userRepository.findAll().forEach(user -> users.add(new UserResponseDto(user)));

            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    @Override
    public ResponseEntity getUserById(int id) {
        try {
            // Id check
            if (!userRepository.existsById(id)) {
                return ResponseEntity.status(404).body("User not found.");
            }

            User user = userRepository.findById(id).get();

            return ResponseEntity.ok(new UserResponseDto(user));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    @Override
    public ResponseEntity deleteUserById(int id) {
        try {
            // Id check
            if (!userRepository.existsById(id)) {
                return ResponseEntity.status(404).body("User not found.");
            }

            // Deleting
            userRepository.deleteById(id);

            return ResponseEntity.ok("User deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    @Override
    public ResponseEntity searchUsers(UserSearchRequestDto userParams) {
        ArrayList<UserResponseDto> users = new ArrayList<>();

        // Searching
        userRepository.searchUsers(
                userParams.getUsername(),
                userParams.getName(),
                userParams.getLastName(),
                userParams.getUserType(),
                userParams.getUserGroup()
        ).forEach(user -> users.add(new UserResponseDto(user)));

        return ResponseEntity.ok(users);
    }
}
