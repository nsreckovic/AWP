package com.ns.awp.user.controller;

import com.ns.awp.config.JwtUtil;
import com.ns.awp.user.models.dto.JwtDto;
import com.ns.awp.user.models.dto.UserLoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtTokenUtil;
    private final UserDetailsService userService;

    @PostMapping("/generateToken")
    public ResponseEntity<?> authenticate(@RequestBody UserLoginDto userLoginDto) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginDto.getUsername(), userLoginDto.getPassword()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(400).body("Incorrect username or password.");
        }

        final UserDetails userDetails = userService.loadUserByUsername(userLoginDto.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtDto(jwt));
    }
}
