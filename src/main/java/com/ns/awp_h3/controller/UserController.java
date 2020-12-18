package com.ns.awp_h3.controller;

import com.ns.awp_h3.dto.NewUserRequestDto;
import com.ns.awp_h3.dto.UserSearchRequestDto;
import com.ns.awp_h3.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasAnyRole('REGULAR', 'ADMIN')")
    @PutMapping("/")
    public ResponseEntity<?> updateUser(@RequestBody NewUserRequestDto user) {
        return userService.updateUser(user);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        return userService.getAllUsers();
    }

    @PreAuthorize("hasAnyRole('REGULAR', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getAllUsers(@PathVariable("id") int id) {
        return userService.getUserById(id);
    }

    @PreAuthorize("hasAnyRole('REGULAR', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") int id) {
        return userService.deleteUserById(id);
    }

    @PreAuthorize("hasAnyRole('REGULAR', 'ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<?> searchUsers(@RequestBody UserSearchRequestDto userParams) {
        return userService.searchUsers(userParams);
    }
}
