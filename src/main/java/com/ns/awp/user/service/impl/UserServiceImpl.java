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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final UserTypeRepository userTypeRepository;
    private final JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public ResponseEntity<?> newUser(UserSaveRequestDto user) {
        try {
            // Null check
            if (user.getUsername() == null) {
                return ResponseEntity.status(400).body("Username cannot be null.");
            } else if (user.getName() == null) {
                return ResponseEntity.status(400).body("Name cannot be null.");
            } else if (user.getLastName() == null) {
                return ResponseEntity.status(400).body("Last name cannot be null.");
            } else if (user.getPassword() == null) {
                return ResponseEntity.status(400).body("Password cannot be null.");
            }

            // Username check
            if (userRepository.existsByUsername(user.getUsername())) {
                return ResponseEntity.status(400).body("User with provided username already exists.");
            }

            // Password check
            if (user.getPassword().length() < 6 || !user.getPassword().matches(".*[0-9].*") || !user.getPassword().matches(".*[A-Za-z].*")) {
                return ResponseEntity.status(400).body("Password must have at least 6 characters and contain letters and numbers.");
            }

            // UserType check
            UserType userType;
            // Every new user is registered as a regular user
            userType = userTypeRepository.findByName("REGULAR").get();

            // Save
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
            // JWT check
            User authenticated = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            if (jwtUtil.hasRole("ROLE_REGULAR") && authenticated.getId() != user.getId()) {
                return ResponseEntity.status(401).body("You cannot change another user's data.");
            }

            // Id check
            User existing = authenticated;
            if (jwtUtil.hasRole("ROLE_ADMIN")) {
                if (user.getId() == null || !userRepository.existsById(user.getId())) {
                    return ResponseEntity.status(404).body("User with provided id not found.");
                }
                existing = userRepository.findById(user.getId()).get();
            }

            // Password check
            if (jwtUtil.hasRole("ROLE_REGULAR")) {
                if (user.getPassword() != null) {
                    if (!bCryptPasswordEncoder.matches(user.getPassword(), existing.getPassword())) {
                        return ResponseEntity.status(401).body("Wrong password. Unauthorized update request.");
                    }
                } else {
                    return ResponseEntity.status(400).body("Password cannot be null.");
                }
            }

            // Username check
            if (user.getUsername() != null) {
                if (!existing.getUsername().equals(user.getUsername()) && userRepository.existsByUsername(user.getUsername())) {
                    return ResponseEntity.status(400).body("User with provided username already exists.");
                } else existing.setUsername(user.getUsername());
            }

            // UserType check
            if (user.getUserType() != null) {
                if (jwtUtil.hasRole("ROLE_ADMIN")) {
                    if (!userTypeRepository.existsByName(user.getUserType())) {
                        return ResponseEntity.status(404).body("User Type with provided name not found.");
                    } else existing.setUserType(userTypeRepository.findByName(user.getUserType()).get());
                } else {
                    return ResponseEntity.status(400).body("You cannot change your user type.");
                }
            }

            // New Password check
            if (user.getNewPassword() != null) {
                if (user.getNewPassword().length() < 6 || !user.getNewPassword().matches(".*[0-9].*") || !user.getNewPassword().matches(".*[A-Za-z].*")) {
                    return ResponseEntity.status(400).body("New password must have at least 6 characters and contain letters and numbers.");
                }
                existing.setPassword(bCryptPasswordEncoder.encode(user.getNewPassword()));
            }

            // Name and LastName
            if (user.getName() != null) existing.setName(user.getName());
            if (user.getLastName() != null) existing.setLastName(user.getLastName());

            // Update
            userRepository.save(existing);

            final UserDetails userDetails = loadUserByUsername(existing.getUsername());
            final String jwt = jwtUtil.generateToken(userDetails);

            return ResponseEntity.ok(new UserWithJwtDto(new UserResponseDto(existing), new JwtDto(jwt)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    @Override
    public ResponseEntity<?> getAllUsers() {
        try {
            List<UserResponseDto> users = new ArrayList<>();

            // Get all
            userRepository.findAll().forEach(user -> users.add(new UserResponseDto(user)));

            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    @Override
    public ResponseEntity<?> getUserById(int id) {
        try {
            // JWT check
            User authenticated = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            if (jwtUtil.hasRole("ROLE_REGULAR") && authenticated.getId() != id) {
                return ResponseEntity.status(401).body("You cannot see other users' data.");
            }

            // Id check
            if (!userRepository.existsById(id)) {
                return ResponseEntity.status(404).body("User not found.");
            }

            // Get by id
            User user = userRepository.findById(id).get();

            return ResponseEntity.ok(new UserResponseDto(user));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    @Override
    public ResponseEntity<?> deleteUserById(int id) {
        try {
            // JWT check
            User authenticated = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            if (jwtUtil.hasRole("ROLE_REGULAR") && authenticated.getId() != id) {
                return ResponseEntity.status(401).body("You cannot delete other users' profile.");
            }

            // Id check
            if (jwtUtil.hasRole("ROLE_ADMIN")) {
                if (!userRepository.existsById(id)) {
                    return ResponseEntity.status(404).body("User not found.");
                }
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
