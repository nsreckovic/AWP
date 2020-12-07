package com.ns.awp_h3.service;

import com.ns.awp_h3.models.StudentGroup;
import org.springframework.http.ResponseEntity;

public interface StudentGroupService {
    ResponseEntity newStudentGroup(StudentGroup studentGroup);

    ResponseEntity updateStudentGroup(StudentGroup studentGroup);

    ResponseEntity getAllStudentGroups();

    ResponseEntity getStudentGroupById(int id);

    ResponseEntity deleteStudentGroup(int id);
}
