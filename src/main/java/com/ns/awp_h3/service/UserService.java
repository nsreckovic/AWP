package com.ns.awp_h3.service;

import com.ns.awp_h3.dto.NewUserRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

public interface UserService {
    ResponseEntity newUser(NewUserRequestDto user);

    ResponseEntity updateUser(NewUserRequestDto user);

    ResponseEntity getAllUsers();

    ResponseEntity getUserById(int id);

    ResponseEntity deleteUserById(int id);
}
