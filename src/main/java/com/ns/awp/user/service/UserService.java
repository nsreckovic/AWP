package com.ns.awp.user.service;

import com.ns.awp.config.security.JsonMessage;
import com.ns.awp.config.security.JwtUtil;
import com.ns.awp.user.models.User;
import com.ns.awp.user.models.dto.UserRequestDto;
import com.ns.awp.user.models.dto.UserResponseDto;
import com.ns.awp.user.models.dto.UserWithJwtResponseDto;
import com.ns.awp.user.repository.UserRepository;
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
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserTypeRepository userTypeRepository;
    private final JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public ResponseEntity<?> newUser(UserRequestDto user) {
        try {
            // Validation
            if (user.getUsername() == null || user.getUsername().isBlank()) {
                return ResponseEntity.status(400).body("Username cannot be null or blank.");
            } else if (user.getName() == null || user.getName().isBlank()) {
                return ResponseEntity.status(400).body("Name cannot be null or blank.");
            } else if (user.getLastName() == null || user.getLastName().isBlank()) {
                return ResponseEntity.status(400).body("Last name cannot be null or blank.");
            } else if (user.getPassword() == null || user.getPassword().isBlank()) {
                return ResponseEntity.status(400).body("Password cannot be null or blank.");
            }

            // Username
            if (userRepository.existsByUsername(user.getUsername())) {
                return ResponseEntity.status(400).body("User with provided username already exists.");
            }

            // Password
            if (user.getPassword().length() < 6 || !user.getPassword().matches(".*[0-9].*") || !user.getPassword().matches(".*[A-Za-z].*")) {
                return ResponseEntity.status(400).body("Password must have at least 6 characters and contain letters and numbers.");
            }

            // UserType
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

    public ResponseEntity<?> updateUser(UserRequestDto user) {
        try {
            // JWT check
            User authenticated = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            if (jwtUtil.hasRole("ROLE_REGULAR") && authenticated.getId() != user.getId()) {
                return ResponseEntity.status(401).body("You cannot change another user's data.");
            }

            // Validation
            User existing = null;
            if (jwtUtil.hasRole("ROLE_ADMIN")) {
                if (user.getId() == null || !userRepository.existsById(user.getId())) {
                    return ResponseEntity.status(404).body("User with provided id not found.");
                }
                existing = userRepository.findById(user.getId()).get();
            } else if (jwtUtil.hasRole("ROLE_REGULAR")) {
                if (user.getPassword() != null) {
                    if (bCryptPasswordEncoder.matches(user.getPassword(), authenticated.getPassword())) {
                        existing = authenticated;
                    } else {
                        return ResponseEntity.status(401).body("Wrong password. Unauthorized update request.");
                    }
                } else {
                    return ResponseEntity.status(400).body("Password cannot be null.");
                }
            }

            // Username validation
            if (user.getUsername() != null && !user.getUsername().isBlank()) {
                if (!existing.getUsername().equals(user.getUsername()) && userRepository.existsByUsername(user.getUsername())) {
                    return ResponseEntity.status(400).body("User with provided username already exists.");
                } else existing.setUsername(user.getUsername());
            }

            // UserType validation
            if (user.getUserType() != null) {
                if (jwtUtil.hasRole("ROLE_ADMIN")) {
                    if (!userTypeRepository.existsByName(user.getUserType())) {
                        return ResponseEntity.status(404).body("User Type with provided name not found.");
                    } else existing.setUserType(userTypeRepository.findByName(user.getUserType()).get());
                } else if (jwtUtil.hasRole("ROLE_REGULAR")) {
                    return ResponseEntity.status(401).body("Regular users cannot change their user type.");
                }
            }

            // New Password validation
            if (user.getNewPassword() != null) {
                if (user.getNewPassword().length() < 6 || !user.getNewPassword().matches(".*[0-9].*") || !user.getNewPassword().matches(".*[A-Za-z].*")) {
                    return ResponseEntity.status(400).body("New password must have at least 6 characters and contain letters and numbers.");
                }
                existing.setPassword(bCryptPasswordEncoder.encode(user.getNewPassword()));
            }

            // Name and LastName
            if (user.getName() != null && !user.getName().isBlank()) existing.setName(user.getName());
            if (user.getLastName() != null && !user.getLastName().isBlank()) existing.setLastName(user.getLastName());

            // Save
            userRepository.save(existing);

            final UserDetails userDetails = loadUserByUsername(existing.getUsername());
            final String jwt = jwtUtil.generateToken(userDetails);

            return ResponseEntity.ok(new UserWithJwtResponseDto(new UserResponseDto(existing), jwt));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    public ResponseEntity<?> getAllUsers() {
        try {
            List<UserResponseDto> users = new ArrayList<>();

            // Get all
            userRepository.findAllSorted().forEach(user -> users.add(new UserResponseDto(user)));

            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    public ResponseEntity<?> getUserById(int id) {
        try {
            // JWT check
            User authenticated = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            if (jwtUtil.hasRole("ROLE_REGULAR") && authenticated.getId() != id) {
                return ResponseEntity.status(401).body("You cannot see other users' data.");
            }

            // Validation
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

    public ResponseEntity<?> getReservationCountByUserId(Integer id) {
        try {
            // JWT check
            User authenticated = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            if (jwtUtil.hasRole("ROLE_REGULAR") && authenticated.getId() != id) {
                return ResponseEntity.status(401).body("You cannot see other users' data.");
            }

            // Validation
            if (!userRepository.existsById(id)) {
                return ResponseEntity.status(404).body("User with provided id not found.");
            }

            // Get by id
            User user = userRepository.findById(id).get();

            return ResponseEntity.ok(user.getReservations().size());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    public ResponseEntity<?> deleteUserById(int id) {
        try {
            // JWT check
            User authenticated = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            if (jwtUtil.hasRole("ROLE_REGULAR") && authenticated.getId() != id) {
                return ResponseEntity.status(401).body("You cannot delete other users' profile.");
            }

            // Validation
            if (jwtUtil.hasRole("ROLE_ADMIN")) {
                if (!userRepository.existsById(id)) {
                    return ResponseEntity.status(404).body("User with provided id not found.");
                }
            }

            // Delete
            userRepository.deleteById(id);

            return ResponseEntity.ok(new JsonMessage("User deleted."));
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
