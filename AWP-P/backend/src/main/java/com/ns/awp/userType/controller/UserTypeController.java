package com.ns.awp.userType.controller;

import com.ns.awp.userType.models.UserType;
import com.ns.awp.userType.service.UserTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/userTypes")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class UserTypeController {
    private final UserTypeService userTypeService;

    @PostMapping
    public ResponseEntity<?> newUserType(@RequestBody UserType userType) {
        return userTypeService.newUserType(userType);
    }

    @PutMapping
    public ResponseEntity<?> updateUserType(@RequestBody UserType userType) {
        return userTypeService.updateUserType(userType);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUserTypes() {
        return userTypeService.getAllUserTypes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserTypeById(@PathVariable("id") int id) {
        return userTypeService.getUserTypeById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserType(@PathVariable("id") int id) {
        return userTypeService.deleteUserTypeById(id);
    }
}
