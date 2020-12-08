package com.ns.awp_h3.service;

import com.ns.awp_h3.dto.NewUserRequestDto;
import com.ns.awp_h3.dto.UserSearchRequestDto;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity newUser(NewUserRequestDto user);

    ResponseEntity updateUser(NewUserRequestDto user);

    ResponseEntity getAllUsers();

    ResponseEntity getUserById(int id);

    ResponseEntity deleteUserById(int id);

    ResponseEntity searchUsers(UserSearchRequestDto userParams);
}
