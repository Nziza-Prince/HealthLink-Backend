package com.healthlinkteam.healthlink.controller;

import com.healthlinkteam.healthlink.dto.CreatePaymentDTO;
import com.healthlinkteam.healthlink.dto.PaymentDTO;
import com.healthlinkteam.healthlink.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @PostMapping
    public ResponseEntity<PaymentDTO> createPayment(@RequestBody CreatePaymentDTO createDTO) {
        return ResponseEntity.ok(paymentService.createPayment(createDTO));
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<PaymentDTO>> getOverduePayments() {
        return ResponseEntity.ok(paymentService.getOverduePayments());
    }

    @PostMapping("/mark-paid/{id}")
    public ResponseEntity<String> markPaymentAsPaid(@PathVariable UUID id) {
        paymentService.markPaymentAsPaid(id);
        return ResponseEntity.ok("Payment marked as paid");
    }
}
