package com.healthlinkteam.healthlink.service;

import com.healthlinkteam.healthlink.dto.PatientOverviewDto;
import com.healthlinkteam.healthlink.dto.PatientSignupDto;
import com.healthlinkteam.healthlink.dto.WalletDto;
import com.healthlinkteam.healthlink.dto.PatientProfileDto;
import com.healthlinkteam.healthlink.entity.*;
import com.healthlinkteam.healthlink.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of PatientService interface.
 * Contains the business logic for all patient-related operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final WalletRepository walletRepository;
    private final AppointmentRepository appointmentRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final PaymentRepository paymentRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<?> signup(PatientSignupDto signupDto) {
        try {
            // Check if passwords match
            if (!signupDto.getPassword().equals(signupDto.getConfirmPassword())) {
                return ResponseEntity.badRequest().body("Passwords do not match");
            }

            // Check if email already exists
            if (patientRepository.existsByEmail(signupDto.getEmail())) {
                return ResponseEntity.badRequest().body("Email already registered");
            }

            // Create new patient
            Patient patient = new Patient();
            patient.setFirstName(signupDto.getFirstName());
            patient.setLastName(signupDto.getLastName());
            patient.setEmail(signupDto.getEmail());
            patient.setCountryOfResidence(signupDto.getCountryOfResidence());
            patient.setPhoneNumber(signupDto.getPhoneNumber());
            patient.setPasswordHash(passwordEncoder.encode(signupDto.getPassword()));

            // Save patient (this will save both User and Patient records due to JOINED inheritance)
            Patient savedPatient = patientRepository.save(patient);

            // Create wallet for patient
            Wallet wallet = new Wallet();
            wallet.setPatient(savedPatient);
            wallet.setBalance(BigDecimal.ZERO);
            walletRepository.save(wallet);

            log.info("Patient registered successfully: {}", savedPatient.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body("Patient registered successfully");

        } catch (Exception e) {
            log.error("Error during patient signup: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed");
        }
    }

    @Override
    public PatientOverviewDto getPatientOverview(UUID patientId) {
        PatientOverviewDto overview = new PatientOverviewDto();
        
        try {
            Optional<Patient> patientOpt = patientRepository.findById(patientId);
            if (patientOpt.isEmpty()) {
                throw new RuntimeException("Patient not found");
            }

            Patient patient = patientOpt.get();

            // Get queue status
            overview.setQueueStatus(getQueueStatus(patientId));

            // Get assigned doctor
            overview.setAssignedDoctor(getAssignedDoctor(patientId));

            // Get total visits
            overview.setTotalVisits(getTotalVisits(patientId));

            // Get wallet balance
            overview.setWalletBalance(getWalletBalance(patientId));

            // Get recent visits
            overview.setRecentVisits(getRecentVisits(patientId));

            // Get upcoming appointments
            overview.setUpcomingAppointments(getUpcomingAppointments(patientId));

            // Get notifications
            overview.setNotifications(getNotifications(patientId));

        } catch (Exception e) {
            log.error("Error getting patient overview: {}", e.getMessage());
        }

        return overview;
    }

    @Override
    public ResponseEntity<?> getPatientAppointments(UUID patientId) {
        try {
            List<Appointment> appointments = appointmentRepository.findByPatientIdOrderByAppointmentDateDesc(patientId);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            log.error("Error getting patient appointments: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get appointments");
        }
    }

    @Override
    public ResponseEntity<?> createAppointment(UUID patientId, String departmentName, String reason, String preferredDate) {
        try {
            // Implementation for creating appointment
            // This would include validation, scheduling logic, etc.
            // For now, just return success - in real implementation, you'd:
            // 1. Validate department exists
            // 2. Check doctor availability
            // 3. Create appointment record
            // 4. Send notifications
            return ResponseEntity.ok("Appointment created successfully for " + departmentName);
        } catch (Exception e) {
            log.error("Error creating appointment: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create appointment");
        }
    }

    @Override
    public ResponseEntity<?> getPatientPrescriptions(UUID patientId) {
        try {
            List<Prescription> prescriptions = prescriptionRepository.findByPatientIdOrderByStartDateDesc(patientId);
            return ResponseEntity.ok(prescriptions);
        } catch (Exception e) {
            log.error("Error getting patient prescriptions: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get prescriptions");
        }
    }

    @Override
    public ResponseEntity<?> getPatientWallet(UUID patientId) {
        try {
            Optional<Wallet> walletOpt = walletRepository.findByPatientId(patientId);
            if (walletOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            Wallet wallet = walletOpt.get();
            // Map to DTO and include recent transactions
            WalletDto dto = new WalletDto();
            dto.setId(wallet.getId());
            dto.setBalance(wallet.getBalance());
            dto.setCurrency(wallet.getCurrency());
            dto.setUpdatedAt(wallet.getUpdatedAt());

            List<Transaction> txs = transactionRepository.findByPatientIdOrderByCreatedAtDesc(patientId);
            List<WalletDto.WalletTransactionDto> recent = new ArrayList<>();
            for (int i = 0; i < Math.min(10, txs.size()); i++) {
                Transaction t = txs.get(i);
                WalletDto.WalletTransactionDto td = new WalletDto.WalletTransactionDto();
                td.setId(t.getId());
                td.setType(t.getType());
                td.setAmount(t.getAmount());
                td.setDescription(t.getDescription());
                td.setReference(t.getReference());
                td.setStatus(t.getStatus());
                td.setCreatedAt(t.getCreatedAt());
                recent.add(td);
            }
            dto.setRecentTransactions(recent);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            log.error("Error getting patient wallet: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get wallet");
        }
    }

    @Override
    public ResponseEntity<?> topUpWallet(UUID patientId, String amount, String paymentMethod) {
        try {
            BigDecimal topUpAmount = new BigDecimal(amount);
            Optional<Wallet> walletOpt = walletRepository.findByPatientId(patientId);
            if (walletOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Wallet not found");
            }
            Wallet wallet = walletOpt.get();
            wallet.addFunds(topUpAmount);
            walletRepository.save(wallet);

            // Create a Payment record (optional placeholder)
            Payment payment = new Payment();
            payment.setPatient(wallet.getPatient());
            payment.setAmount(topUpAmount);
            payment.setPaymentMethod(com.healthlinkteam.healthlink.enums.PaymentMethod.valueOf(paymentMethod));
            payment.setStatus(com.healthlinkteam.healthlink.enums.PaymentStatus.COMPLETED);
            payment.setDescription("Wallet top-up");
            payment.setPaidDate(LocalDateTime.now());
            Payment savedPayment = paymentRepository.save(payment);

            // Record Transaction
            Transaction tx = new Transaction();
            tx.setPatient(wallet.getPatient());
            tx.setPayment(savedPayment);
            tx.setType("TOP_UP");
            tx.setAmount(topUpAmount);
            tx.setDescription("Wallet top-up via " + paymentMethod);
            tx.setReference(savedPayment.getReferenceNumber());
            tx.setStatus("COMPLETED");
            transactionRepository.save(tx);

            // Return updated wallet DTO
            return getPatientWallet(patientId);
        } catch (Exception e) {
            log.error("Error topping up wallet: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to top up wallet");
        }
    }

    @Override
    public ResponseEntity<?> getPatientPayments(UUID patientId) {
        try {
            List<Payment> payments = paymentRepository.findByPatientIdOrderByCreatedAtDesc(patientId);
            return ResponseEntity.ok(payments);
        } catch (Exception e) {
            log.error("Error getting patient payments: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get payments");
        }
    }

    @Override
    public ResponseEntity<?> getPatientTransactions(UUID patientId) {
        try {
            // Implementation for getting transactions
            // This would include wallet transactions, payment history, etc.
            return ResponseEntity.ok("Transactions retrieved successfully");
        } catch (Exception e) {
            log.error("Error getting patient transactions: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get transactions");
        }
    }

    @Override
    public ResponseEntity<?> getPatientProfile(UUID patientId) {
        try {
            Optional<Patient> patientOpt = patientRepository.findById(patientId);
            if (patientOpt.isPresent()) {
                Patient patient = patientOpt.get();
                PatientProfileDto profileDto = convertToPatientProfileDto(patient);
                return ResponseEntity.ok(profileDto);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error getting patient profile: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get profile");
        }
    }

    @Override
    public ResponseEntity<?> updatePatientProfile(UUID patientId, PatientSignupDto profileDto) {
        try {
            Optional<Patient> patientOpt = patientRepository.findById(patientId);
            if (patientOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Patient patient = patientOpt.get();
            patient.setFirstName(profileDto.getFirstName());
            patient.setLastName(profileDto.getLastName());
            patient.setCountryOfResidence(profileDto.getCountryOfResidence());
            patient.setPhoneNumber(profileDto.getPhoneNumber());

            Patient updatedPatient = patientRepository.save(patient);
            return ResponseEntity.ok(updatedPatient);

        } catch (Exception e) {
            log.error("Error updating patient profile: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update profile");
        }
    }

    // Conversion methods
    private PatientProfileDto convertToPatientProfileDto(Patient patient) {
        PatientProfileDto dto = new PatientProfileDto();
        dto.setId(patient.getId());
        dto.setEmail(patient.getEmail());
        dto.setFirstName(patient.getFirstName());
        dto.setLastName(patient.getLastName());
        dto.setDateOfBirth(patient.getDateOfBirth());
        dto.setGender(patient.getGender());
        dto.setAddress(patient.getAddress());
        dto.setEmergencyContactName(patient.getEmergencyContactName());
        dto.setEmergencyContactPhone(patient.getEmergencyContactPhone());
        dto.setPhoneNumber(patient.getPhoneNumber());
        dto.setCountryOfResidence(patient.getCountryOfResidence());
        dto.setActive(patient.getIsActive());
        dto.setCreatedAt(patient.getCreatedAt());
        dto.setUpdatedAt(patient.getUpdatedAt());
        
        // Add wallet info if available
        Optional<Wallet> walletOpt = walletRepository.findByPatientId(patient.getId());
        if (walletOpt.isPresent()) {
            Wallet wallet = walletOpt.get();
            dto.setWalletId(wallet.getId());
            dto.setWalletBalance(wallet.getBalance().toString());
            dto.setWalletCurrency(wallet.getCurrency());
        }
        
        return dto;
    }

    // Helper methods for overview
    private PatientOverviewDto.QueueStatusDto getQueueStatus(UUID patientId) {
        // Implementation to get queue status
        PatientOverviewDto.QueueStatusDto queueStatus = new PatientOverviewDto.QueueStatusDto();
        queueStatus.setStatus("Not in queue");
        queueStatus.setDepartment("N/A");
        queueStatus.setPosition(0);
        queueStatus.setEstimatedWaitTime("N/A");
        return queueStatus;
    }

    private PatientOverviewDto.AssignedDoctorDto getAssignedDoctor(UUID patientId) {
        // Implementation to get assigned doctor
        PatientOverviewDto.AssignedDoctorDto assignedDoctor = new PatientOverviewDto.AssignedDoctorDto();
        assignedDoctor.setDoctorName("No doctor assigned");
        assignedDoctor.setDepartment("N/A");
        assignedDoctor.setHospitalName("N/A");
        assignedDoctor.setSpecialization("N/A");
        return assignedDoctor;
    }

    private PatientOverviewDto.TotalVisitsDto getTotalVisits(UUID patientId) {
        // Implementation to get total visits
        PatientOverviewDto.TotalVisitsDto totalVisits = new PatientOverviewDto.TotalVisitsDto();
        totalVisits.setTotalVisits(0);
        totalVisits.setDepartment("All");
        totalVisits.setThisMonth(0);
        totalVisits.setThisYear(0);
        return totalVisits;
    }

    private BigDecimal getWalletBalance(UUID patientId) {
        Optional<Wallet> walletOpt = walletRepository.findByPatientId(patientId);
        return walletOpt.map(Wallet::getBalance).orElse(BigDecimal.ZERO);
    }

    private List<PatientOverviewDto.RecentVisitDto> getRecentVisits(UUID patientId) {
        // Implementation to get recent visits
        return new ArrayList<>();
    }

    private List<PatientOverviewDto.UpcomingAppointmentDto> getUpcomingAppointments(UUID patientId) {
        // Implementation to get upcoming appointments
        return new ArrayList<>();
    }

    private List<PatientOverviewDto.NotificationDto> getNotifications(UUID patientId) {
        // Implementation to get notifications
        return new ArrayList<>();
    }
} 