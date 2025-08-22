package com.healthlinkteam.healthlink.service;

import com.healthlinkteam.healthlink.dto.LoginDto;
import com.healthlinkteam.healthlink.dto.LoginResponseDto;
import com.healthlinkteam.healthlink.dto.PatientSignupDto;
import com.healthlinkteam.healthlink.entity.Patient;
import com.healthlinkteam.healthlink.repository.PatientRepository;
import com.healthlinkteam.healthlink.service.PatientService;
import com.healthlinkteam.healthlink.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of AuthService interface.
 * Provides basic authentication functionality.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final PatientService patientService;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public ResponseEntity<LoginResponseDto> login(LoginDto loginDto) {
        try {
            log.info("Login attempt for email: {}", loginDto.getEmail());
            
            // Find patient by email
            Optional<Patient> patientOpt = patientRepository.findByEmail(loginDto.getEmail());
            if (patientOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponseDto(null, null, null, null, "Invalid email or password"));
            }
            
            Patient patient = patientOpt.get();
            
            // Verify password
            if (!passwordEncoder.matches(loginDto.getPassword(), patient.getPasswordHash())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponseDto(null, null, null, null, "Invalid email or password"));
            }
            
            // Generate JWT token
            String token = jwtUtil.generateToken(patient.getEmail(), patient.getRole().name());
            
            // Create response
            LoginResponseDto response = new LoginResponseDto(
                token,
                "Bearer",
                patient.getEmail(),
                patient.getRole().name(),
                "Login successful"
            );
            
            log.info("Login successful for email: {}", loginDto.getEmail());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error during login: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new LoginResponseDto(null, null, null, null, "Login failed"));
        }
    }

    @Override
    public ResponseEntity<?> patientSignup(PatientSignupDto signupDto) {
        // Delegate to patient service
        return patientService.signup(signupDto);
    }

    @Override
    public ResponseEntity<?> refreshToken(String refreshToken) {
        // Basic implementation - in production, this would validate and refresh JWT
        log.info("Token refresh requested");
        return ResponseEntity.ok("Token refreshed successfully");
    }

    @Override
    public ResponseEntity<?> logout(String token) {
        // Basic implementation - in production, this would invalidate JWT
        log.info("Logout requested");
        return ResponseEntity.ok("Logged out successfully");
    }
} 