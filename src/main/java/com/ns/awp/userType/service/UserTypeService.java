package com.ns.awp.userType.service;

import com.ns.awp.userType.models.UserType;
import org.springframework.http.ResponseEntity;

public interface UserTypeService {
    ResponseEntity<?> newUserType(UserType userType);

    ResponseEntity<?> updateUserType(UserType userType);

    ResponseEntity<?> getAllUserTypes();

    ResponseEntity<?> getUserTypeById(int id);

    ResponseEntity<?> deleteUserTypeById(int id);
}
