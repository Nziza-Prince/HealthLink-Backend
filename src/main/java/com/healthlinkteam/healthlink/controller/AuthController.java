package com.healthlinkteam.healthlink.controller;

import com.healthlinkteam.healthlink.dto.*;
import com.healthlinkteam.healthlink.entity.User;
import com.healthlinkteam.healthlink.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/patient/signup")
    public ResponseEntity<?> patientSignup(@RequestBody PatientSignupDto request) {
        AuthResponse patient = authService.patientSignup(request);
        if (patient == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Signup failed");
        }
        return ResponseEntity.ok(patient);
    }

    @PostMapping("/doctor/signup")
    public ResponseEntity<?> doctorSignup(@RequestBody DoctorSignupDto request) {
        AuthResponse doctor = authService.doctorSignup(request);
        if (doctor == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Signup failed");
        }
        return ResponseEntity.ok(doctor);
    }

    @PostMapping("/manager/signup")
    public ResponseEntity<?> managerSignup(@RequestBody ManagerSignupDto request) {
        AuthResponse manager = authService.managerSignup(request);
        if (manager == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Signup failed");
        }
        return ResponseEntity.ok(manager);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto request) {
        AuthResponse response = authService.login(request);
        if(response == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Login failed");
        } else {
            LoginResponseDto loginResponse = new LoginResponseDto();
            loginResponse.setAccessToken(response.getAccessToken());
            loginResponse.setEmail(request.getEmail());
            loginResponse.setRole(request.getRole().toString());

            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        }
    }

    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody TokenRefresh request) {
        AuthResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }

    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@AuthenticationPrincipal UserDetails userDetails) {
        User user = authService.getUserByUsername(userDetails.getUsername());
        authService.logout(user);
        return ResponseEntity.ok("Logged out successfully");
    }
}
