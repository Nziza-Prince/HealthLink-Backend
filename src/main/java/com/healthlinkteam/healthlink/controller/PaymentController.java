package com.healthlinkteam.healthlink.controller;

import com.healthlinkteam.healthlink.dto.CreatePaymentDTO;
import com.healthlinkteam.healthlink.dto.PaymentDTO;
import com.healthlinkteam.healthlink.entity.Manager;
import com.healthlinkteam.healthlink.repository.ManagerRepository;
import com.healthlinkteam.healthlink.service.PaymentService;
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
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Tag(name = "Payment")
@SecurityRequirement(name = "bearerAuth")
public class PaymentController {

    private final PaymentService paymentService;
    private final ManagerRepository managerRepository;

    @GetMapping
    public ResponseEntity<?> getAllPayments(Authentication authentication) {
        // Extract email from JWT token
        String email = authentication.getName();
        // Find patient by email
        Optional<Manager> manager = managerRepository.findByEmail(email);
        if (manager.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Manager not found");
        }
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @PostMapping
    public ResponseEntity<?> createPayment(
            @RequestBody CreatePaymentDTO createDTO,
            Authentication authentication
            ) {
        // Extract email from JWT token
        String email = authentication.getName();
        // Find patient by email
        Optional<Manager> manager = managerRepository.findByEmail(email);
        if (manager.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Manager not found");
        }
        return ResponseEntity.ok(paymentService.createPayment(createDTO));
    }

    @GetMapping("/overdue")
    public ResponseEntity<?> getOverduePayments(Authentication authentication) {
        // Extract email from JWT token
        String email = authentication.getName();
        // Find patient by email
        Optional<Manager> manager = managerRepository.findByEmail(email);
        if (manager.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Manager not found");
        }
        return ResponseEntity.ok(paymentService.getOverduePayments());
    }

    @PostMapping("/mark-paid/{id}")
    public ResponseEntity<String> markPaymentAsPaid(
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
        paymentService.markPaymentAsPaid(id);
        return ResponseEntity.ok("Payment marked as paid");
    }
}
