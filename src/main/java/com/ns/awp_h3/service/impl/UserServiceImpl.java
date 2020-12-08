package com.ns.awp_h3.service.impl;

import com.ns.awp_h3.dto.NewUserRequestDto;
import com.ns.awp_h3.dto.StudentGroupResponseDto;
import com.ns.awp_h3.dto.UserResponseDto;
import com.ns.awp_h3.dto.UserTypeResponseDto;
import com.ns.awp_h3.models.StudentGroup;
import com.ns.awp_h3.models.User;
import com.ns.awp_h3.models.UserType;
import com.ns.awp_h3.repository.StudentGroupRepository;
import com.ns.awp_h3.repository.UserRepository;
import com.ns.awp_h3.repository.UserTypeRepository;
import com.ns.awp_h3.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserTypeRepository userTypeRepository;
    private final StudentGroupRepository studentGroupRepository;

    @Override
    public ResponseEntity newUser(NewUserRequestDto user) {
        try {
            if (userTypeRepository.existsById(user.getUserType()) && studentGroupRepository.existsById(user.getStudentGroup())) {
                UserType userType = userTypeRepository.findById(user.getUserType()).get();
                StudentGroup studentGroup = studentGroupRepository.findById(user.getStudentGroup()).get();

                User saved = userRepository.save(new User(
                        -1,
                        user.getUsername(),
                        user.getPassword(),
                        user.getName(),
                        user.getLastName(),
                        userType,
                        studentGroup
                ));

                userType.getUsers().add(saved);
                userTypeRepository.save(userType);
                studentGroup.getUsers().add(saved);
                studentGroupRepository.save(studentGroup);

                UserResponseDto responseDto = new UserResponseDto(
                        saved.getId(),
                        saved.getUsername(),
                        saved.getName(),
                        saved.getLastName(),
                        new UserTypeResponseDto(saved.getUserType()),
                        new StudentGroupResponseDto(studentGroup)
                );
                return ResponseEntity.ok(responseDto);
            } else {
                return ResponseEntity.status(404).body("User Type or Student Group not found.");
            }
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(400).body("Invalid request body.");
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    @Override
    public ResponseEntity updateUser(NewUserRequestDto user) {
        return null;
    }

    @Override
    public ResponseEntity getAllUsers() {
        List<UserResponseDto> users = new ArrayList();
        userRepository.findAll().forEach(user -> {
            users.add(new UserResponseDto(
                    user.getId(),
                    user.getUsername(),
                    user.getName(),
                    user.getLastName(),
                    new UserTypeResponseDto(user.getUserType()),
                    new StudentGroupResponseDto(user.getStudentGroup())
            ));
        });
        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity getUserById(int id) {
        return null;
    }

    @Override
    public ResponseEntity deleteUserById(int id) {
        return null;
    }
}
