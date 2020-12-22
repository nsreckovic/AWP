package com.ns.awp.user.service;

import com.ns.awp.user.models.dto.UserRequestDto;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> newUser(UserRequestDto user);

    ResponseEntity<?> updateUser(UserRequestDto user);

    ResponseEntity<?> getAllUsers();

    ResponseEntity<?> getUserById(int id);

    ResponseEntity<?> deleteUserById(int id);
}
