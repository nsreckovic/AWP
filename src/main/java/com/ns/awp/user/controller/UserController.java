package com.ns.awp.user.controller;

import com.ns.awp.user.models.dto.UserRequestDto;
import com.ns.awp.user.service.UserService;
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
    public ResponseEntity<?> newUser(@RequestBody UserRequestDto user) {
        return userService.newUser(user);
    }

    @PreAuthorize("hasAnyRole('REGULAR', 'ADMIN')")
    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody UserRequestDto user) {
        return userService.updateUser(user);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        return userService.getAllUsers();
    }

    @PreAuthorize("hasAnyRole('REGULAR', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") int id) {
        return userService.getUserById(id);
    }

    @PreAuthorize("hasAnyRole('REGULAR', 'ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") int id) {
        return userService.deleteUserById(id);
    }
}
