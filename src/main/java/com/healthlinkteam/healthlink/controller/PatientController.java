package com.healthlinkteam.healthlink.controller;

import com.healthlinkteam.healthlink.dto.CreateAppointmentDto;
import com.healthlinkteam.healthlink.dto.DepartmentDto;
import com.healthlinkteam.healthlink.dto.PatientOverviewDto;
import com.healthlinkteam.healthlink.dto.PatientSignupDto;
import com.healthlinkteam.healthlink.dto.TopUpWalletDto;
import com.healthlinkteam.healthlink.entity.Patient;
import com.healthlinkteam.healthlink.repository.PatientRepository;
import com.healthlinkteam.healthlink.security.JwtUtils;
import com.healthlinkteam.healthlink.service.PatientService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST Controller for patient-related operations.
 * Handles patient signup, overview, appointments, and other patient functionalities.
 */
@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Patient")
@SecurityRequirement(name = "bearerAuth")
public class PatientController {

    private final PatientService patientService;
    private final PatientRepository patientRepository;
    private final JwtUtils jwtUtil;

    /**
     * Patient signup endpoint
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody PatientSignupDto signupDto) {
        return patientService.signup(signupDto);
    }

    /**
     * Get patient overview/dashboard
     */
    @GetMapping("/overview")
    public ResponseEntity<?> getOverview(Authentication authentication) {
        String email = authentication.getName();
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found"));
        if (patient == null) {
            return ResponseEntity.badRequest().body("patientId parameter is required");
        }
        PatientOverviewDto overview = patientService.getPatientOverview(patient.getId());
        return ResponseEntity.ok(overview);
    }

    /**
     * Test endpoint to verify the API is working
     */
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Patient API is working!");
    }

    /**
     * Get all departments for frontend dropdown
     */
    @GetMapping("/departments")
    public ResponseEntity<?> getAllDepartments(Authentication authentication) {
        String email = authentication.getName();
        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found"));
        if (patient == null) {
            return ResponseEntity.badRequest().body("patientId parameter is required");
        }
        var departments = patientService.getDepartments();
        return ResponseEntity.ok(departments);
    }

    /**
     * Get patient overview/dashboard (authenticated - gets patientId from JWT)
     */
    @GetMapping("/overview/me")
    public ResponseEntity<?> getMyOverview(Authentication authentication) {
        // Extract email from JWT token
        String email = authentication.getName();
        // Find patient by email
        Optional<Patient> patientOpt = patientRepository.findByEmail(email);
        if (patientOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Patient not found");
        }

        PatientOverviewDto overview = patientService.getPatientOverview(patientOpt.get().getId());
        return ResponseEntity.ok(overview);
    }

    /**
     * Get patient appointments (with patientId parameter - for development/testing)
     */
    @GetMapping("/appointments")
    public ResponseEntity<?> getAppointments(
            @RequestParam UUID patientId,
            Authentication authentication
            ) {
        // Extract email from JWT token
        String email = authentication.getName();
        // Find patient by email
        Optional<Patient> patientOpt = patientRepository.findByEmail(email);
        if (patientOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Patient not found");
        }
        return patientService.getPatientAppointments(patientId);
    }

    /**
     * Get current user's appointments (authenticated - gets patientId from JWT)
     */
    @GetMapping("/appointments/me")
    public ResponseEntity<?> getMyAppointments(Authentication authentication) {
        // Extract email from JWT token
        String email = authentication.getName();
        // Find patient by email
        Optional<Patient> patientOpt = patientRepository.findByEmail(email);
        if (patientOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Patient not found");
        }

        return patientService.getPatientAppointments(patientOpt.get().getId());
    }

    /**
     * Create new appointment (with patientId in JSON body - for development/testing)
     */
    @PostMapping("/appointments")
    public ResponseEntity<?> createAppointment(
            @Valid @RequestBody CreateAppointmentDto appointmentDto,
            Authentication authentication
            ) {
        // Extract email from JWT token
        String email = authentication.getName();
        // Find patient by email
        Optional<Patient> patientOpt = patientRepository.findByEmail(email);
        if (patientOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Patient not found");
        }
        UUID patientId = UUID.fromString(appointmentDto.getPatientId());
        return patientService.createAppointment(
            patientId,
            appointmentDto.getDepartmentName(),
            appointmentDto.getReason(),
            appointmentDto.getPreferredDate()
        );
    }

    /**
     * Create new appointment for current user (authenticated - gets patientId from JWT)
     */
    @PostMapping("/appointments/me")
    public ResponseEntity<?> createMyAppointment(Authentication authentication,
                                               @Valid @RequestBody CreateAppointmentDto appointmentDto) {
        String email = authentication.getName();
        Optional<Patient> patientOpt = patientRepository.findByEmail(email);
        if (patientOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Patient not found");
        }

        return patientService.createAppointment(
            patientOpt.get().getId(),
            appointmentDto.getDepartmentName(),
            appointmentDto.getReason(),
            appointmentDto.getPreferredDate()
        );
    }

    /**
     * Get patient prescriptions (with patientId parameter - for development/testing)
     */
    @GetMapping("/prescriptions")
    public ResponseEntity<?> getPrescriptions(
            @RequestParam UUID patientId,
            Authentication authentication
            ) {
        // Extract email from JWT token
        String email = authentication.getName();
        // Find patient by email
        Optional<Patient> patientOpt = patientRepository.findByEmail(email);
        if (patientOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Patient not found");
        }
        return patientService.getPatientPrescriptions(patientId);
    }

    /**
     * Get current user's prescriptions (authenticated - gets patientId from JWT)
     */
    @GetMapping("/prescriptions/me")
    public ResponseEntity<?> getMyPrescriptions(Authentication authentication) {
        String email = authentication.getName();
        Optional<Patient> patientOpt = patientRepository.findByEmail(email);
        if (patientOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Patient not found");
        }

        return patientService.getPatientPrescriptions(patientOpt.get().getId());
    }

    /**
     * Get patient wallet information (with patientId parameter - for development/testing)
     */
    @GetMapping("/wallet")
    public ResponseEntity<?> getWallet(
            @RequestParam UUID patientId,
            Authentication authentication
            ) {
        // Extract email from JWT token
        String email = authentication.getName();
        // Find patient by email
        Optional<Patient> patientOpt = patientRepository.findByEmail(email);
        if (patientOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Patient not found");
        }
        return patientService.getPatientWallet(patientId);
    }

    /**
     * Get current user's wallet (authenticated - gets patientId from JWT)
     */
    @GetMapping("/wallet/me")
    public ResponseEntity<?> getMyWallet(Authentication authentication) {
        String email = authentication.getName();
        Optional<Patient> patientOpt = patientRepository.findByEmail(email);
        if (patientOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Patient not found");
        }

        return patientService.getPatientWallet(patientOpt.get().getId());
    }

    /**
     * Top up wallet (with patientId in JSON body - for development/testing)
     */
    @PostMapping("/wallet/topup")
    public ResponseEntity<?> topUpWallet(
            @Valid @RequestBody TopUpWalletDto topUpDto,
            Authentication authentication
            ) {
        // Extract email from JWT token
        String email = authentication.getName();
        // Find patient by email
        Optional<Patient> patientOpt = patientRepository.findByEmail(email);
        if (patientOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Patient not found");
        }
        UUID patientId = UUID.fromString(topUpDto.getPatientId());
        return patientService.topUpWallet(
            patientId,
            topUpDto.getAmount().toString(),
            topUpDto.getPaymentMethod()
        );
    }

    /**
     * Top up current user's wallet (authenticated - gets patientId from JWT)
     */
    @PostMapping("/wallet/topup/me")
    public ResponseEntity<?> topUpMyWallet(Authentication authentication,
                                         @Valid @RequestBody TopUpWalletDto topUpDto) {

        String email = authentication.getName();
        Optional<Patient> patientOpt = patientRepository.findByEmail(email);
        if (patientOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Patient not found");
        }

        return patientService.topUpWallet(
            patientOpt.get().getId(),
            topUpDto.getAmount().toString(),
            topUpDto.getPaymentMethod()
        );
    }

    /**
     * Get patient payments (with patientId parameter - for development/testing)
     */
    @GetMapping("/payments")
    public ResponseEntity<?> getPayments(
            @RequestParam UUID patientId,
            Authentication authentication
            ) {
        // Extract email from JWT token
        String email = authentication.getName();
        // Find patient by email
        Optional<Patient> patientOpt = patientRepository.findByEmail(email);
        if (patientOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Patient not found");
        }
        return patientService.getPatientPayments(patientId);
    }

    /**
     * Get current user's payments (authenticated - gets patientId from JWT)
     */
    @GetMapping("/payments/me")
    public ResponseEntity<?> getMyPayments(Authentication authentication) {
        String email = authentication.getName();
        Optional<Patient> patientOpt = patientRepository.findByEmail(email);
        if (patientOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Patient not found");
        }

        return patientService.getPatientPayments(patientOpt.get().getId());
    }

    /**
     * Get patient transactions (with patientId parameter - for development/testing)
     */
    @GetMapping("/transactions")
    public ResponseEntity<?> getTransactions(
            @RequestParam UUID patientId,
            Authentication authentication
            ) {
        // Extract email from JWT token
        String email = authentication.getName();
        // Find patient by email
        Optional<Patient> patientOpt = patientRepository.findByEmail(email);
        if (patientOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Patient not found");
        }
        return patientService.getPatientTransactions(patientId);
    }

    /**
     * Get current user's transactions (authenticated - gets patientId from JWT)
     */
    @GetMapping("/transactions/me")
    public ResponseEntity<?> getMyTransactions(Authentication authentication) {
        // Extract email from JWT token
        String email = authentication.getName();
        // Find patient by email
        Optional<Patient> patientOpt = patientRepository.findByEmail(email);
        if (patientOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Patient not found");
        }

        return patientService.getPatientTransactions(patientOpt.get().getId());
    }

    /**
     * Get patient profile (with patientId parameter - for development/testing)
     */
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(
            @RequestParam UUID patientId,
            Authentication authentication
            ) {
        // Extract email from JWT token
        String email = authentication.getName();
        // Find patient by email
        Optional<Patient> patientOpt = patientRepository.findByEmail(email);
        if (patientOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Patient not found");
        }
        return patientService.getPatientProfile(patientId);
    }

    /**
     * Get current user's profile (authenticated - gets patientId from JWT)
     */
    @GetMapping("/profile/me")
    public ResponseEntity<?> getMyProfile(Authentication authentation) {
        String email = authentation.getName();
        Optional<Patient> patientOpt = patientRepository.findByEmail(email);
        if (patientOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Patient not found");
        }

        return patientService.getPatientProfile(patientOpt.get().getId());
    }

    /**
     * Update patient profile
     */
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestParam UUID patientId,
                                        @RequestBody PatientSignupDto profileDto,
                                        Authentication authentication
                                           ) {
        // Extract email from JWT token
        String email = authentication.getName();
        // Find patient by email
        Optional<Patient> patientOpt = patientRepository.findByEmail(email);
        if (patientOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Patient not found");
        }
        return patientService.updatePatientProfile(patientId, profileDto);
    }
} 