package com.ns.awp.userType.service.impl;

import com.ns.awp.config.JsonMessage;
import com.ns.awp.userType.models.UserType;
import com.ns.awp.userType.repository.UserTypeRepository;
import com.ns.awp.userType.service.UserTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserTypeServiceImpl implements UserTypeService {
    private final UserTypeRepository userTypeRepository;

    @Override
    public ResponseEntity<?> newUserType(UserType userType) {
        try {
            userType.setName(userType.getName().toUpperCase());

            // Saving
            userTypeRepository.save(userType);

            return ResponseEntity.ok(userType);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(400).body("User Type name must be non null and unique.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    @Override
    public ResponseEntity<?> updateUserType(UserType userType) {
        try {
            // Id check
            if (!userTypeRepository.existsById(userType.getId())) {
                return ResponseEntity.status(404).body("User Type with provided id not found.");
            }
            UserType existingUserType = userTypeRepository.findById(userType.getId()).get();

            // Name check
            if (userTypeRepository.existsByName(userType.getName())) {
                return ResponseEntity.status(400).body("User Type with provided name already exists.");
            }

            // Updating
            existingUserType.setName(userType.getName().toUpperCase());
            userTypeRepository.save(existingUserType);

            return ResponseEntity.ok(existingUserType);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    @Override
    public ResponseEntity<?> getAllUserTypes() {
        try {
            // Getting all
            Iterable<UserType> userTypes = userTypeRepository.findAll();

            return ResponseEntity.ok(userTypes);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    @Override
    public ResponseEntity<?> getUserTypeById(int id) {
        try {
            // Getting by id
            UserType userType = userTypeRepository.findById(id).get();

            return ResponseEntity.ok(userType);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("User Type with provided id not found.");
        }
    }

    @Override
    public ResponseEntity<?> deleteUserTypeById(int id) {
        try {
            // Id check
            if (!userTypeRepository.existsById(id)) {
                ResponseEntity.status(404).body("User Type with provided id not found.");
            }

            // Deleting
            userTypeRepository.deleteById(id);

            return ResponseEntity.ok(new JsonMessage("User Type deleted."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }
}
