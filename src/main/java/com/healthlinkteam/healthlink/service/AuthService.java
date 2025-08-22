package com.healthlinkteam.healthlink.service;

import com.healthlinkteam.healthlink.dto.LoginDoctor;
import com.healthlinkteam.healthlink.dto.LoginResponseDto;
import com.healthlinkteam.healthlink.dto.RegisterDoctor;
import com.healthlinkteam.healthlink.entity.Doctor;
import com.healthlinkteam.healthlink.repository.DoctorRepository;
import com.healthlinkteam.healthlink.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final DoctorRepository doctorRepository;
    private final DoctorService doctorService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public ResponseEntity<LoginResponseDto> login(LoginDoctor loginDto) {
        try {
            log.info("Login attempt for email: {}", loginDto.getEmail());

            // Find patient by email
            Optional<Doctor> doctorOpt = doctorRepository.findByEmail(loginDto.getEmail());
            if (doctorOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new LoginResponseDto(null, null, null, null, "Invalid email or password"));
            }

            Doctor doctor = doctorOpt.get();

            // Verify password
            if (!passwordEncoder.matches(loginDto.getPassword(), doctor.getPasswordHash())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new LoginResponseDto(null, null, null, null, "Invalid email or password"));
            }

            // Generate JWT token
            String token = jwtUtil.generateToken(doctor.getEmail(), doctor.getRole().name());

            // Create response
            LoginResponseDto response = new LoginResponseDto(
                    token,
                    "Bearer",
                    doctor.getEmail(),
                    doctor.getRole().name(),
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

    public ResponseEntity<?> doctorSignup(RegisterDoctor signupDto) {
        // Delegate to patient service
        return doctorService.signup(signupDto);
    }

    public ResponseEntity<?> refreshToken(String refreshToken) {
        // Basic implementation - in production, this would validate and refresh JWT
        log.info("Token refresh requested");
        return ResponseEntity.ok("Token refreshed successfully");
    }

    public ResponseEntity<?> logout(String token) {
        // Basic implementation - in production, this would invalidate JWT
        log.info("Logout requested");
        return ResponseEntity.ok("Logged out successfully");
    }
}
