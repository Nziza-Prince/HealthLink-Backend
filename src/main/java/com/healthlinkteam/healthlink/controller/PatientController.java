package com.healthlinkteam.healthlink.controller;

import com.healthlinkteam.healthlink.dto.CreateAppointmentDto;
import com.healthlinkteam.healthlink.dto.DepartmentDto;
import com.healthlinkteam.healthlink.dto.PatientOverviewDto;
import com.healthlinkteam.healthlink.dto.PatientSignupDto;
import com.healthlinkteam.healthlink.dto.TopUpWalletDto;
import com.healthlinkteam.healthlink.entity.Patient;
import com.healthlinkteam.healthlink.repository.PatientRepository;
import com.healthlinkteam.healthlink.service.PatientService;
import com.healthlinkteam.healthlink.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
public class PatientController {

    private final PatientService patientService;
    private final PatientRepository patientRepository;
    private final JwtUtil jwtUtil;

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
    public ResponseEntity<?> getOverview(@RequestParam(required = false) UUID patientId) {
        // For now, allow access without authentication
        // In production, this would get the patientId from the JWT token
        if (patientId == null) {
            return ResponseEntity.badRequest().body("patientId parameter is required");
        }
        PatientOverviewDto overview = patientService.getPatientOverview(patientId);
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
    public ResponseEntity<List<DepartmentDto>> getAllDepartments() {
        // This would typically call a service method
        // For now, return a sample list
        List<DepartmentDto> departments = List.of(
            new DepartmentDto() {{
                setId(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"));
                setName("Cardiology");
                setDescription("Heart and cardiovascular system");
                setHospitalName("General Hospital");
                setIsActive(true);
            }},
            new DepartmentDto() {{
                setId(UUID.fromString("550e8400-e29b-41d4-a716-446655440002"));
                setName("Orthopedics");
                setDescription("Bones, joints, and muscles");
                setHospitalName("General Hospital");
                setIsActive(true);
            }},
            new DepartmentDto() {{
                setId(UUID.fromString("550e8400-e29b-41d4-a716-446655440003"));
                setName("Pediatrics");
                setDescription("Children's health");
                setHospitalName("General Hospital");
                setIsActive(true);
            }},
            new DepartmentDto() {{
                setId(UUID.fromString("550e8400-e29b-41d4-a716-446655440004"));
                setName("General Medicine");
                setDescription("General health and wellness");
                setHospitalName("General Hospital");
                setIsActive(true);
            }},
            new DepartmentDto() {{
                setId(UUID.fromString("550e8400-e29b-41d4-a716-446655440005"));
                setName("Emergency Medicine");
                setDescription("Urgent care and emergencies");
                setHospitalName("General Hospital");
                setIsActive(true);
            }}
        );
        return ResponseEntity.ok(departments);
    }

    /**
     * Get patient overview/dashboard (authenticated - gets patientId from JWT)
     */
    @GetMapping("/overview/me")
    public ResponseEntity<?> getMyOverview(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Authorization header required");
        }
        
        String token = authHeader.substring(7); // Remove "Bearer " prefix
        try {
            // Extract email from JWT token
            String email = jwtUtil.extractUsername(token);
            // Find patient by email
            Optional<Patient> patientOpt = patientRepository.findByEmail(email);
            if (patientOpt.isEmpty()) {
                return ResponseEntity.status(404).body("Patient not found");
            }
            
            PatientOverviewDto overview = patientService.getPatientOverview(patientOpt.get().getId());
            return ResponseEntity.ok(overview);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid token");
        }
    }

    /**
     * Get patient appointments (with patientId parameter - for development/testing)
     */
    @GetMapping("/appointments")
    public ResponseEntity<?> getAppointments(@RequestParam UUID patientId) {
        return patientService.getPatientAppointments(patientId);
    }

    /**
     * Get current user's appointments (authenticated - gets patientId from JWT)
     */
    @GetMapping("/appointments/me")
    public ResponseEntity<?> getMyAppointments(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Authorization header required");
        }
        
        String token = authHeader.substring(7); // Remove "Bearer " prefix
        try {
            // Extract email from JWT token
            String email = jwtUtil.extractUsername(token);
            // Find patient by email
            Optional<Patient> patientOpt = patientRepository.findByEmail(email);
            if (patientOpt.isEmpty()) {
                return ResponseEntity.status(404).body("Patient not found");
            }
            
            return patientService.getPatientAppointments(patientOpt.get().getId());
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid token");
        }
    }

    /**
     * Create new appointment (with patientId in JSON body - for development/testing)
     */
    @PostMapping("/appointments")
    public ResponseEntity<?> createAppointment(@Valid @RequestBody CreateAppointmentDto appointmentDto) {
        if (appointmentDto.getPatientId() == null) {
            return ResponseEntity.badRequest().body("patientId is required in request body");
        }
        try {
            UUID patientId = UUID.fromString(appointmentDto.getPatientId());
            return patientService.createAppointment(
                patientId, 
                appointmentDto.getDepartmentName(), 
                appointmentDto.getReason(), 
                appointmentDto.getPreferredDate()
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid patientId format");
        }
    }

    /**
     * Create new appointment for current user (authenticated - gets patientId from JWT)
     */
    @PostMapping("/appointments/me")
    public ResponseEntity<?> createMyAppointment(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                               @Valid @RequestBody CreateAppointmentDto appointmentDto) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Authorization header required");
        }
        
        String token = authHeader.substring(7);
        try {
            String email = jwtUtil.extractUsername(token);
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
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid token");
        }
    }

    /**
     * Get patient prescriptions (with patientId parameter - for development/testing)
     */
    @GetMapping("/prescriptions")
    public ResponseEntity<?> getPrescriptions(@RequestParam UUID patientId) {
        return patientService.getPatientPrescriptions(patientId);
    }

    /**
     * Get current user's prescriptions (authenticated - gets patientId from JWT)
     */
    @GetMapping("/prescriptions/me")
    public ResponseEntity<?> getMyPrescriptions(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Authorization header required");
        }
        
        String token = authHeader.substring(7);
        try {
            String email = jwtUtil.extractUsername(token);
            Optional<Patient> patientOpt = patientRepository.findByEmail(email);
            if (patientOpt.isEmpty()) {
                return ResponseEntity.status(404).body("Patient not found");
            }
            
            return patientService.getPatientPrescriptions(patientOpt.get().getId());
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid token");
        }
    }

    /**
     * Get patient wallet information (with patientId parameter - for development/testing)
     */
    @GetMapping("/wallet")
    public ResponseEntity<?> getWallet(@RequestParam UUID patientId) {
        return patientService.getPatientWallet(patientId);
    }

    /**
     * Get current user's wallet (authenticated - gets patientId from JWT)
     */
    @GetMapping("/wallet/me")
    public ResponseEntity<?> getMyWallet(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Authorization header required");
        }
        
        String token = authHeader.substring(7);
        try {
            String email = jwtUtil.extractUsername(token);
            Optional<Patient> patientOpt = patientRepository.findByEmail(email);
            if (patientOpt.isEmpty()) {
                return ResponseEntity.status(404).body("Patient not found");
            }
            
            return patientService.getPatientWallet(patientOpt.get().getId());
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid token");
        }
    }

    /**
     * Top up wallet (with patientId in JSON body - for development/testing)
     */
    @PostMapping("/wallet/topup")
    public ResponseEntity<?> topUpWallet(@Valid @RequestBody TopUpWalletDto topUpDto) {
        if (topUpDto.getPatientId() == null) {
            return ResponseEntity.badRequest().body("patientId is required in request body");
        }
        try {
            UUID patientId = UUID.fromString(topUpDto.getPatientId());
            return patientService.topUpWallet(
                patientId, 
                topUpDto.getAmount().toString(), 
                topUpDto.getPaymentMethod()
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid patientId format");
        }
    }

    /**
     * Top up current user's wallet (authenticated - gets patientId from JWT)
     */
    @PostMapping("/wallet/topup/me")
    public ResponseEntity<?> topUpMyWallet(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                         @Valid @RequestBody TopUpWalletDto topUpDto) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Authorization header required");
        }
        
        String token = authHeader.substring(7);
        try {
            String email = jwtUtil.extractUsername(token);
            Optional<Patient> patientOpt = patientRepository.findByEmail(email);
            if (patientOpt.isEmpty()) {
                return ResponseEntity.status(404).body("Patient not found");
            }
            
            return patientService.topUpWallet(
                patientOpt.get().getId(), 
                topUpDto.getAmount().toString(), 
                topUpDto.getPaymentMethod()
            );
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid token");
        }
    }

    /**
     * Get patient payments (with patientId parameter - for development/testing)
     */
    @GetMapping("/payments")
    public ResponseEntity<?> getPayments(@RequestParam UUID patientId) {
        return patientService.getPatientPayments(patientId);
    }

    /**
     * Get current user's payments (authenticated - gets patientId from JWT)
     */
    @GetMapping("/payments/me")
    public ResponseEntity<?> getMyPayments(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Authorization header required");
        }
        
        String token = authHeader.substring(7);
        try {
            String email = jwtUtil.extractUsername(token);
            Optional<Patient> patientOpt = patientRepository.findByEmail(email);
            if (patientOpt.isEmpty()) {
                return ResponseEntity.status(404).body("Patient not found");
            }
            
            return patientService.getPatientPayments(patientOpt.get().getId());
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid token");
        }
    }

    /**
     * Get patient transactions (with patientId parameter - for development/testing)
     */
    @GetMapping("/transactions")
    public ResponseEntity<?> getTransactions(@RequestParam UUID patientId) {
        return patientService.getPatientTransactions(patientId);
    }

    /**
     * Get current user's transactions (authenticated - gets patientId from JWT)
     */
    @GetMapping("/transactions/me")
    public ResponseEntity<?> getMyTransactions(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Authorization header required");
        }
        
        String token = authHeader.substring(7);
        try {
            String email = jwtUtil.extractUsername(token);
            Optional<Patient> patientOpt = patientRepository.findByEmail(email);
            if (patientOpt.isEmpty()) {
                return ResponseEntity.status(404).body("Patient not found");
            }
            
            return patientService.getPatientTransactions(patientOpt.get().getId());
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid token");
        }
    }

    /**
     * Get patient profile (with patientId parameter - for development/testing)
     */
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestParam UUID patientId) {
        return patientService.getPatientProfile(patientId);
    }

    /**
     * Get current user's profile (authenticated - gets patientId from JWT)
     */
    @GetMapping("/profile/me")
    public ResponseEntity<?> getMyProfile(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Authorization header required");
        }
        
        String token = authHeader.substring(7);
        try {
            String email = jwtUtil.extractUsername(token);
            Optional<Patient> patientOpt = patientRepository.findByEmail(email);
            if (patientOpt.isEmpty()) {
                return ResponseEntity.status(404).body("Patient not found");
            }
            
            return patientService.getPatientProfile(patientOpt.get().getId());
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid token");
        }
    }

    /**
     * Update patient profile
     */
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestParam UUID patientId,
                                        @RequestBody PatientSignupDto profileDto) {
        return patientService.updatePatientProfile(patientId, profileDto);
    }
} 