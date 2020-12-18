package com.ns.awp.user.service.impl;

import com.ns.awp.config.JwtUtil;
import com.ns.awp.user.models.User;
import com.ns.awp.user.models.dto.JwtDto;
import com.ns.awp.user.models.dto.UserSaveRequestDto;
import com.ns.awp.user.models.dto.UserResponseDto;
import com.ns.awp.user.models.dto.UserWithJwtDto;
import com.ns.awp.user.repository.UserRepository;
import com.ns.awp.user.service.UserService;
import com.ns.awp.userType.models.UserType;
import com.ns.awp.userType.repository.UserTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final UserTypeRepository userTypeRepository;
    private final JwtUtil jwtTokenUtil;

     @Autowired
     private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public ResponseEntity<?> newUser(UserSaveRequestDto user) {
        try {
            // Username check
            if (userRepository.existsByUsername(user.getUsername())) {
                return ResponseEntity.status(400).body("User with provided username already exists.");
            }

            // UserType check
            UserType userType;
            if (!userTypeRepository.existsById(user.getUserType())) {
                return ResponseEntity.status(400).body("User Type with provided id not found.");
            } else userType = userTypeRepository.findById(user.getUserType()).get();

            // Saving
            User saved = userRepository.save(new User(
                    -1,
                    user.getUsername(),
                    bCryptPasswordEncoder.encode(user.getPassword()),
                    user.getName(),
                    user.getLastName(),
                    userType,
                    null,
                    null
            ));

            return ResponseEntity.ok(new UserResponseDto(saved));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(400).body("Invalid request body.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    @Override
    public ResponseEntity<?> updateUser(UserSaveRequestDto user) {
        try {
            // Id check
            if (user.getId() == null || !userRepository.existsById(user.getId())) {
                return ResponseEntity.status(404).body("User with provided id not found.");
            }
            User existing = userRepository.findById(user.getId()).get();

            // Password check
            if (!bCryptPasswordEncoder.matches(user.getPassword(), existing.getPassword())) {
                return ResponseEntity.status(401).body("Wrong password. Unauthorized update request.");
            }

            // Username check
            if (!existing.getUsername().equals(user.getUsername()) && userRepository.existsByUsername(user.getUsername())) {
                return ResponseEntity.status(400).body("User with provided username already exists.");
            } else existing.setUsername(user.getUsername());

            // UserType check
            if (!userTypeRepository.existsById(user.getUserType())) {
                return ResponseEntity.status(400).body("User Type with provided id not found.");
            } else existing.setUserType(userTypeRepository.findById(user.getUserType()).get());

            // New Password check
            if (user.getNewPassword() != null) {
                existing.setPassword(bCryptPasswordEncoder.encode(user.getNewPassword()));
            }

            // Name and LastName
            existing.setName(user.getName());
            existing.setLastName(user.getLastName());

            // Updating
            userRepository.save(existing);

            final UserDetails userDetails = loadUserByUsername(existing.getUsername());
            final String jwt = jwtTokenUtil.generateToken(userDetails);

            return ResponseEntity.ok(new UserWithJwtDto(new UserResponseDto(existing), new JwtDto(jwt)));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    @Override
    public ResponseEntity<?> getAllUsers() {
        try {
            List<UserResponseDto> users = new ArrayList<>();

            // Getting all
            userRepository.findAll().forEach(user -> users.add(new UserResponseDto(user)));

            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    @Override
    public ResponseEntity<?> getUserById(int id) {
        try {
            // Id check
            if (!userRepository.existsById(id)) {
                return ResponseEntity.status(404).body("User not found.");
            }

            // Getting by id
            User user = userRepository.findById(id).get();

            return ResponseEntity.ok(new UserResponseDto(user));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    @Override
    public ResponseEntity<?> deleteUserById(int id) {
        try {
            // Id check
            if (!userRepository.existsById(id)) {
                return ResponseEntity.status(404).body("User not found.");
            }

            // Deleting
            userRepository.deleteById(id);

            return ResponseEntity.ok("User deleted.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) throw new UsernameNotFoundException("Invalid username or password.");
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthority(user));
    }

    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getUserType().getName()));
        return authorities;
    }

}
