package com.healthlinkteam.healthlink.controller;

import com.healthlinkteam.healthlink.dto.LoginDoctor;
import com.healthlinkteam.healthlink.dto.LoginResponseDto;
import com.healthlinkteam.healthlink.dto.RegisterDoctor;
import com.healthlinkteam.healthlink.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/doctor/v1/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * User login endpoint (for all user types)
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginDoctor loginDto) {
        return authService.login(loginDto);
    }

    /**
     * Patient signup endpoint
     */
    @PostMapping("/doctor/signup")
    public ResponseEntity<?> doctorSignup(@Valid @RequestBody RegisterDoctor signupDto) {
        return authService.doctorSignup(signupDto);
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
