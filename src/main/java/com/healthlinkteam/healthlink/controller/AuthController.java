package com.healthlinkteam.healthlink.controller;

import com.healthlinkteam.healthlink.dto.*;
import com.healthlinkteam.healthlink.entity.User;
import com.healthlinkteam.healthlink.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/patient/signup")
    public ResponseEntity<User> patientSignup(@RequestBody PatientSignup request) {
        User patient = authService.patientSignup(request);
        return ResponseEntity.ok(patient);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody PatientLogin request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody TokenRefresh request) {
        AuthResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@AuthenticationPrincipal UserDetails userDetails) {
        User user = authService.getUserByUsername(userDetails.getUsername());
        authService.logout(user);
        return ResponseEntity.ok("Logged out successfully");
    }
}