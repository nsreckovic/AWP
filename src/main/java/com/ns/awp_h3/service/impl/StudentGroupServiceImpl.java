package com.ns.awp_h3.service.impl;

import com.ns.awp_h3.models.StudentGroup;
import com.ns.awp_h3.repository.StudentGroupRepository;
import com.ns.awp_h3.service.StudentGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentGroupServiceImpl implements StudentGroupService {
    private final StudentGroupRepository studentGroupRepository;

    @Override
    public ResponseEntity newStudentGroup(StudentGroup studentGroup) {
        try {
            studentGroupRepository.save(studentGroup);
            return ResponseEntity.ok(studentGroup);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(400).body("Student Group name must be non null and unique.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    @Override
    public ResponseEntity updateStudentGroup(StudentGroup studentGroup) {
        try {
            if (studentGroupRepository.existsById(studentGroup.getId())) {
                studentGroupRepository.save(studentGroup);
                return ResponseEntity.ok(studentGroup);
            } else return ResponseEntity.status(404).body("Student Group with provided id not found.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    @Override
    public ResponseEntity getAllStudentGroups() {
        try {
            Iterable<StudentGroup> studentGroups = studentGroupRepository.findAll();
            return ResponseEntity.ok(studentGroups);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    @Override
    public ResponseEntity getStudentGroupById(int id) {
        try {
            StudentGroup studentGroup = studentGroupRepository.findById(id).get();
            return ResponseEntity.ok(studentGroup);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Student Group with provided id not found.");
        }
    }

    @Override
    public ResponseEntity deleteStudentGroup(int id) {
        try {
            if (studentGroupRepository.existsById(id)) {
                studentGroupRepository.deleteById(id);
                return ResponseEntity.ok("Student Group deleted.");
            } else return ResponseEntity.status(404).body("Student Group with provided id not found.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }
}
