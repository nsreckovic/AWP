package com.ns.awp_h3.controller;

import com.ns.awp_h3.dto.NewUserRequestDto;
import com.ns.awp_h3.dto.UserSearchRequestDto;
import com.ns.awp_h3.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.web.servlet.SecurityMarker;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> newUser(@RequestBody NewUserRequestDto user) {
        return userService.newUser(user);
    }

    @PutMapping("/")
    public ResponseEntity<?> updateUser(@RequestBody NewUserRequestDto user) {
        return userService.updateUser(user);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAllUsers(@PathVariable("id") int id) {
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") int id) {
        return userService.deleteUserById(id);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchUsers(@RequestBody UserSearchRequestDto userParams) {
        return userService.searchUsers(userParams);
    }
}
