package com.ns.awp_h3.controller;

import com.ns.awp_h3.models.StudentGroup;
import com.ns.awp_h3.repository.StudentGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/studentGroups")
@RequiredArgsConstructor
public class StudentGroupController {
    private final StudentGroupRepository studentGroupRepository;

    @PostMapping("/new")
    public String addNewUserType(@RequestBody StudentGroup studentGroup) {
        studentGroupRepository.save(studentGroup);
        return "Saved";
    }

    @GetMapping("/all")
    public Iterable<StudentGroup> getAllStudentGroups() {
        return studentGroupRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public String deleteStudentGroup(@PathVariable("id") int id) {
        studentGroupRepository.deleteById(id);
        return "Deleted.";
    }
}
