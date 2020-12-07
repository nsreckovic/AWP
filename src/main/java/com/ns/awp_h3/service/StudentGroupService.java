package com.ns.awp_h3.service;

import com.ns.awp_h3.models.UserType;
import org.springframework.http.ResponseEntity;

public interface StudentGroupService {
    ResponseEntity newStudentGroup(UserType userType);

    ResponseEntity updateStudentGroup(UserType userType);

    ResponseEntity getAllStudentGroups();

    ResponseEntity getStudentGroupById(int id);

    ResponseEntity deleteStudentGroup(int id);
}
