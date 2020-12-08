package com.ns.awp_h3.controller;

import com.ns.awp_h3.dto.NewUserRequestDto;
import com.ns.awp_h3.models.StudentGroup;
import com.ns.awp_h3.models.User;
import com.ns.awp_h3.models.UserType;
import com.ns.awp_h3.repository.StudentGroupRepository;
import com.ns.awp_h3.repository.UserRepository;
import com.ns.awp_h3.repository.UserTypeRepository;
import com.ns.awp_h3.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/new")
    public ResponseEntity addNewUser (@RequestBody NewUserRequestDto user) {
        return userService.newUser(user);
    }

    @GetMapping("/all")
    public ResponseEntity getAllUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") int id) {
//        userRepository.deleteById(id);
//        return "Deleted.";
        return null;
    }

}
