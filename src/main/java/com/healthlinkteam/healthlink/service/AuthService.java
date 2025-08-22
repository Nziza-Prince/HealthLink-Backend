package com.healthlinkteam.healthlink.service;

import com.healthlinkteam.healthlink.dto.LoginDto;
import com.healthlinkteam.healthlink.dto.LoginResponseDto;
import com.healthlinkteam.healthlink.dto.PatientSignupDto;
import org.springframework.http.ResponseEntity;

/**
 * Service interface for authentication-related business logic.
 * Defines all authentication operations including login, signup, and token management.
 */
public interface AuthService {

    /**
     * Authenticate user login
     */
    ResponseEntity<LoginResponseDto> login(LoginDto loginDto);

    /**
     * Register new patient
     */
    ResponseEntity<?> patientSignup(PatientSignupDto signupDto);

    /**
     * Refresh authentication token
     */
    ResponseEntity<?> refreshToken(String refreshToken);

    /**
     * Logout user
     */
    ResponseEntity<?> logout(String token);
} 