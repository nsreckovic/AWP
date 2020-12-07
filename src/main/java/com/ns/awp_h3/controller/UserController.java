package com.ns.awp_h3.controller;

import com.ns.awp_h3.models.User;
import com.ns.awp_h3.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping() // Map ONLY POST Requests
    public String addNewUser (@RequestParam String username, @RequestParam String password) {
        User n = new User();
        n.setUsername(username);
        n.setPassword(password);
        userRepository.save(n);
        return "Saved";
    }

    @GetMapping(path="/all")
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

}
