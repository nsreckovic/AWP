package com.ns.awp_h3.controller;

import com.ns.awp_h3.dto.NewUserRequestDto;
import com.ns.awp_h3.models.StudentGroup;
import com.ns.awp_h3.models.User;
import com.ns.awp_h3.models.UserType;
import com.ns.awp_h3.repository.StudentGroupRepository;
import com.ns.awp_h3.repository.UserRepository;
import com.ns.awp_h3.repository.UserTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final UserTypeRepository userTypeRepository;
    private final StudentGroupRepository studentGroupRepository;

    @PostMapping("/new")
    public String addNewUser (@RequestBody NewUserRequestDto user) {
        if (userTypeRepository.existsById(user.getUserType()) && studentGroupRepository.existsById(user.getStudentGroup())) {
            UserType userType = userTypeRepository.findById(user.getUserType()).get();
            StudentGroup studentGroup = studentGroupRepository.findById(user.getStudentGroup()).get();
            User u = userRepository.save(new User(
                    -1,
                    user.getUsername(),
                    user.getPassword(),
                    user.getName(),
                    user.getLastName(),
                    userType,
                    studentGroup
            ));
            studentGroup.getUsers().add(u);
            studentGroupRepository.save(studentGroup);
        } else {
            return "Error";
        }

        return "Saved";
    }

    @GetMapping("/all")
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userRepository.deleteById(id);
        return "Deleted.";
    }

}
