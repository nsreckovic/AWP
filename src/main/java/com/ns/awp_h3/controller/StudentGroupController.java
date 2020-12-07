package com.ns.awp_h3.controller;

import com.ns.awp_h3.models.StudentGroup;
import com.ns.awp_h3.service.StudentGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/studentGroups")
@RequiredArgsConstructor
public class StudentGroupController {
    private final StudentGroupService studentGroupService;

    @PostMapping("/")
    public ResponseEntity addNewStudentGroup(@RequestBody StudentGroup studentGroup) {
        return studentGroupService.newStudentGroup(studentGroup);
    }

    @PutMapping("/")
    public ResponseEntity updateStudentGroup(@RequestBody StudentGroup studentGroup) {
        return studentGroupService.updateStudentGroup(studentGroup);
    }

    @GetMapping("/all")
    public ResponseEntity getAllStudentGroups() {
        return studentGroupService.getAllStudentGroups();
    }

    @GetMapping("/{id}")
    public ResponseEntity getStudentGroupById(@PathVariable("id") int id) {
        return studentGroupService.getStudentGroupById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteStudentGroup(@PathVariable("id") int id) {
        return studentGroupService.deleteStudentGroup(id);
    }
}
