package com.healthlinkteam.healthlink.controller;

import com.healthlinkteam.healthlink.dto.LoginDto;
import com.healthlinkteam.healthlink.dto.LoginResponseDto;
import com.healthlinkteam.healthlink.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for authentication operations.
 * Handles login for patients, doctors, and managers.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    /**
     * User login endpoint (for all user types)
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginDto loginDto) {
        return authService.login(loginDto);
    }

    /**
     * Patient signup endpoint
     */
    @PostMapping("/patient/signup")
    public ResponseEntity<?> patientSignup(@Valid @RequestBody com.healthlinkteam.healthlink.dto.PatientSignupDto signupDto) {
        return authService.patientSignup(signupDto);
    }

    /**
     * Refresh token endpoint
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String refreshToken) {
        return authService.refreshToken(refreshToken);
    }

    /**
     * Logout endpoint
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        return authService.logout(token);
    }
} 