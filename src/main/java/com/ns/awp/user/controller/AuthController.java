package com.ns.awp.user.controller;

import com.ns.awp.config.JwtUtil;
import com.ns.awp.user.models.User;
import com.ns.awp.user.models.dto.UserLoginRequestDto;
import com.ns.awp.user.models.dto.UserResponseDto;
import com.ns.awp.user.models.dto.UserWithJwtResponseDto;
import com.ns.awp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtTokenUtil;
    private final UserDetailsService userService;
    private final UserRepository userRepository;

    @PostMapping("/generateToken")
    public ResponseEntity<?> authenticate(@RequestBody UserLoginRequestDto userLoginRequestDto) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginRequestDto.getUsername(), userLoginRequestDto.getPassword()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(400).body("Incorrect username or password.");
        }

        final UserDetails userDetails = userService.loadUserByUsername(userLoginRequestDto.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);

        User user = userRepository.findByUsername(userLoginRequestDto.getUsername());

        return ResponseEntity.ok(new UserWithJwtResponseDto(new UserResponseDto(user), jwt));
    }
}
