package com.healthlinkteam.healthlink.service;

import com.healthlinkteam.healthlink.dto.CreatePaymentDTO;
import com.healthlinkteam.healthlink.dto.PaymentDTO;
import com.healthlinkteam.healthlink.entity.Payment;
import com.healthlinkteam.healthlink.enums.NotificationType;
import com.healthlinkteam.healthlink.enums.PaymentStatus;
import com.healthlinkteam.healthlink.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final NotificationService notificationService;

    public List<PaymentDTO> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PaymentDTO createPayment(CreatePaymentDTO createDTO) {
        Payment payment = new Payment();
        payment.getPatient().setId(createDTO.getPatientId());
        payment.getAppointment().setId(createDTO.getAppointmentId());
        payment.setAmount(createDTO.getAmount());
        payment.setPaymentMethod(createDTO.getPaymentMethod());
        payment.setPhoneNumber(createDTO.getPhoneNumber());
        payment.setDueDate(createDTO.getDueDate());
        payment.setDescription(createDTO.getDescription());
        payment.setStatus(PaymentStatus.PENDING);

        Payment saved = paymentRepository.save(payment);
        return convertToDTO(saved);
    }

    public List<PaymentDTO> getOverduePayments() {
        return paymentRepository.findOverduePayments().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void markPaymentAsPaid(UUID id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setPaidDate(java.time.LocalDateTime.now());
        paymentRepository.save(payment);
    }

    public void checkAndNotifyOverduePayments() {
        List<Payment> overduePayments = paymentRepository.findOverduePayments();
        for (Payment payment : overduePayments) {
            notificationService.sendSystemNotification(
                    "Overdue payment: " + payment.getDescription() + " - $" + payment.getAmount(),
                    NotificationType.OVERDUE_PAYMENT
            );
        }
    }

    private PaymentDTO convertToDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        dto.setId(payment.getId());
        dto.setPatientId(payment.getPatient().getId());
        dto.setAppointmentId(payment.getAppointment().getId());
        dto.setAmount(payment.getAmount());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setStatus(payment.getStatus());
        dto.setReferenceNumber(payment.getReferenceNumber());
        dto.setPhoneNumber(payment.getPhoneNumber());
        dto.setDueDate(payment.getDueDate());
        dto.setPaidDate(payment.getPaidDate());
        dto.setDescription(payment.getDescription());
        dto.setCreatedAt(payment.getCreatedAt());
        dto.setUpdatedAt(payment.getUpdatedAt());
        return dto;
    }
}
