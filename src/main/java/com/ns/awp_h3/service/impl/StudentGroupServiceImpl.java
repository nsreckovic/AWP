package com.ns.awp_h3.service.impl;

import com.ns.awp_h3.models.UserType;
import com.ns.awp_h3.repository.StudentGroupRepository;
import com.ns.awp_h3.service.StudentGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentGroupServiceImpl implements StudentGroupService {
    private final StudentGroupRepository studentGroupRepository;

    @Override
    public ResponseEntity newStudentGroup(UserType userType) {
        return null;
    }

    @Override
    public ResponseEntity updateStudentGroup(UserType userType) {
        return null;
    }

    @Override
    public ResponseEntity getAllStudentGroups() {
        return null;
    }

    @Override
    public ResponseEntity getStudentGroupById(int id) {
        return null;
    }

    @Override
    public ResponseEntity deleteStudentGroup(int id) {
        return null;
    }
}
