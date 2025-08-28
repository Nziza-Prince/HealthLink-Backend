package com.healthlinkteam.healthlink.controller;

import com.healthlinkteam.healthlink.dto.CreateMedicationDTO;
import com.healthlinkteam.healthlink.dto.MedicationDTO;
import com.healthlinkteam.healthlink.entity.Manager;
import com.healthlinkteam.healthlink.enums.MedicationStatus;
import com.healthlinkteam.healthlink.repository.ManagerRepository;
import com.healthlinkteam.healthlink.service.MedicationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/medications")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Medication")
@SecurityRequirement(name = "bearerAuth")
public class MedicationController {

    private final MedicationService medicationService;
    private final ManagerRepository managerRepository;

    @GetMapping
    public ResponseEntity<?> getAll(Authentication authentication) {
        // Extract email from JWT token
        String email = authentication.getName();
        // Find patient by email
        Optional<Manager> manager = managerRepository.findByEmail(email);
        if (manager.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Manager not found");
        }
        return ResponseEntity.ok(medicationService.getAllMedications());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getByStatus(
            @PathVariable MedicationStatus status,
            Authentication authentication
            ) {
        // Extract email from JWT token
        String email = authentication.getName();
        // Find patient by email
        Optional<Manager> manager = managerRepository.findByEmail(email);
        if (manager.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Manager not found");
        }
        return ResponseEntity.ok(medicationService.getByStatus(status));
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<?> getByAppointment(
            @PathVariable UUID appointmentId,
            Authentication authentication
            ) {
        // Extract email from JWT token
        String email = authentication.getName();
        // Find patient by email
        Optional<Manager> manager = managerRepository.findByEmail(email);
        if (manager.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Manager not found");
        }
        return ResponseEntity.ok(medicationService.getByAppointment(appointmentId));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody CreateMedicationDTO dto,
            Authentication authentication
            ) {
        // Extract email from JWT token
        String email = authentication.getName();
        // Find patient by email
        Optional<Manager> manager = managerRepository.findByEmail(email);
        if (manager.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Manager not found");
        }
        return ResponseEntity.ok(medicationService.createMedication(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable UUID id, @RequestBody CreateMedicationDTO dto,
            Authentication authentication
            ) {
        // Extract email from JWT token
        String email = authentication.getName();
        // Find patient by email
        Optional<Manager> manager = managerRepository.findByEmail(email);
        if (manager.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Manager not found");
        }
        return ResponseEntity.ok(medicationService.updateMedication(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
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
        medicationService.deleteMedication(id);
        return ResponseEntity.ok("Medication deleted successfully");
    }
}
