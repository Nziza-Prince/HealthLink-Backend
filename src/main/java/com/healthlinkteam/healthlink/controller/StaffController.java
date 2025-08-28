package com.healthlinkteam.healthlink.controller;

import com.healthlinkteam.healthlink.dto.CreateUserDTO;
import com.healthlinkteam.healthlink.dto.UserDTO;
import com.healthlinkteam.healthlink.entity.Doctor;
import com.healthlinkteam.healthlink.entity.Manager;
import com.healthlinkteam.healthlink.entity.Patient;
import com.healthlinkteam.healthlink.repository.ManagerRepository;
import com.healthlinkteam.healthlink.service.StaffService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/staff")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Staff")
@SecurityRequirement(name = "bearerAuth")
public class StaffController {

    private final StaffService staffService;
    private final ManagerRepository managerRepository;

    @GetMapping
    public ResponseEntity<?> getAllStaff(Authentication authentication) {
        // Extract email from JWT token
        String email = authentication.getName();
        // Find patient by email
        Optional<Manager> manager = managerRepository.findByEmail(email);
        if (manager.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Manager not found");
        }
        return ResponseEntity.ok(staffService.all());
    }

    @PostMapping
    public ResponseEntity<?> createDoctor(
            @RequestBody Doctor doctor,
            Authentication authentication
            ) {
        // Extract email from JWT token
        String email = authentication.getName();
        // Find patient by email
        Optional<Manager> manager = managerRepository.findByEmail(email);
        if (manager.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Manager not found");
        }
        return ResponseEntity.ok(staffService.create(doctor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDoctor(
            @PathVariable UUID id,
            @RequestBody Doctor doctor,
            Authentication authentication
    ) {
        // Extract email from JWT token
        String email = authentication.getName();
        // Find patient by email
        Optional<Manager> manager = managerRepository.findByEmail(email);
        if (manager.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Manager not found");
        }
        return ResponseEntity.ok(staffService.update(id, doctor));
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivateDoctor(
            @PathVariable UUID id,
            Authentication authentication
            ) {
        // Extract email from JWT token
        String email = authentication.getName();
        // Find patient by email
        Optional<Manager> manager = managerRepository.findByEmail(email);
        if (manager.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Manager not found");
        }
        staffService.deactivate(id);
        return ResponseEntity.ok("Doctor deactivated successfully");
    }
}
