package com.ns.awp.user.service;

import com.ns.awp.user.models.dto.UserSaveRequestDto;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> newUser(UserSaveRequestDto user);

    ResponseEntity<?> updateUser(UserSaveRequestDto user);

    ResponseEntity<?> getAllUsers();

    ResponseEntity<?> getUserById(int id);

    ResponseEntity<?> deleteUserById(int id);
}
