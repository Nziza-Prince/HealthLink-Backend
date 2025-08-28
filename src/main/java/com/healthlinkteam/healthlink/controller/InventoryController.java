package com.healthlinkteam.healthlink.controller;

import com.healthlinkteam.healthlink.dto.CreateMedicalInventoryDTO;
import com.healthlinkteam.healthlink.dto.MedicalInventoryDTO;
import com.healthlinkteam.healthlink.entity.Manager;
import com.healthlinkteam.healthlink.entity.Patient;
import com.healthlinkteam.healthlink.repository.ManagerRepository;
import com.healthlinkteam.healthlink.repository.UserRepository;
import com.healthlinkteam.healthlink.service.InventoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "*")
@Tag(name = "Inventory")
@SecurityRequirement(name = "bearerAuth")
public class InventoryController {

    private final InventoryService inventoryService;
    private final UserRepository userRepository;
    private final ManagerRepository managerRepository;

    public InventoryController(InventoryService inventoryService, UserRepository userRepository, ManagerRepository managerRepository) {
        this.inventoryService = inventoryService;
        this.userRepository = userRepository;
        this.managerRepository = managerRepository;
    }

    @GetMapping
    public ResponseEntity<?> getAllInventory(Authentication authentication) {
        // Extract email from JWT token
        String email = authentication.getName();
        // Find patient by email
        Optional<Manager> manager = managerRepository.findByEmail(email);
        if (manager.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found");
        }
        return ResponseEntity.ok(inventoryService.getAllInventory());
    }

    @PostMapping
    public ResponseEntity<?> addMedication(
            @RequestBody CreateMedicalInventoryDTO createDTO,
            Authentication authentication
            ) {
        // Extract email from JWT token
        String email = authentication.getName();
        // Find patient by email
        Optional<Manager> manager = managerRepository.findByEmail(email);
        if (manager.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found");
        }
        MedicalInventoryDTO saved = inventoryService.addMedication(createDTO);
        return ResponseEntity.created(URI.create("/api/inventory/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateInventory(@PathVariable UUID id,
                                                               @RequestBody CreateMedicalInventoryDTO updateDTO,
                                              Authentication authentication
                                             ) {
        // Extract email from JWT token
        String email = authentication.getName();
        // Find patient by email
        Optional<Manager> manager = managerRepository.findByEmail(email);
        if (manager.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found");
        }
        return ResponseEntity.ok(inventoryService.updateInventory(id, updateDTO));
    }

    @PostMapping("/{id}/reorder")
    public ResponseEntity<String> reorderMedication(@PathVariable UUID id) {
        inventoryService.reorderMedication(id);
        return ResponseEntity.ok("Reorder initiated successfully");
    }

    @GetMapping("/low-stock")
    public ResponseEntity<?> getLowStockItems(Authentication authentication) {
        // Extract email from JWT token
        String email = authentication.getName();
        // Find patient by email
        Optional<Manager> manager = managerRepository.findByEmail(email);
        if (manager.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found");
        }
        return ResponseEntity.ok(inventoryService.getLowStockItems());
    }

    @GetMapping("/by-status/{status}")
    public ResponseEntity<?> getInventoryByStatus(
            @PathVariable String status,
            Authentication authentication
            ) {
        // Extract email from JWT token
        String email = authentication.getName();
        // Find patient by email
        Optional<Manager> manager = managerRepository.findByEmail(email);
        if (manager.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found");
        }
        return ResponseEntity.ok(inventoryService.getInventoryByStatus(status));
    }
}
