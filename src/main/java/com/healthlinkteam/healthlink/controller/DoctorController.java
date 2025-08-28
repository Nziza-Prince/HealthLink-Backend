package com.healthlinkteam.healthlink.controller;

import com.healthlinkteam.healthlink.entity.Appointment;
import com.healthlinkteam.healthlink.entity.Doctor;
import com.healthlinkteam.healthlink.entity.Prescription;
import com.healthlinkteam.healthlink.enums.AppointmentStatus;
import com.healthlinkteam.healthlink.repository.DoctorRepository;
import com.healthlinkteam.healthlink.security.JwtUtils;
import com.healthlinkteam.healthlink.service.DoctorService;
import com.healthlinkteam.healthlink.service.QueueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/doctor")
@Tag(name = "Doctor")
@SecurityRequirement(name = "bearerAuth")
public class DoctorController {

    private final JwtUtils jwtUtil;
    private final DoctorRepository doctorRepository;
    private final DoctorService doctorService;

    public DoctorController(JwtUtils jwtUtil, DoctorRepository doctorRepository, DoctorService doctorService) {
        this.doctorService = doctorService;
        this.jwtUtil = jwtUtil;
        this.doctorRepository = doctorRepository;
    }

    // Get all patients in a doctor's queue
    @GetMapping("/queue")
    public ResponseEntity<?> getQueue(Authentication authentication) {
        // Spring Security gives you the authenticated user automatically
        String email = authentication.getName();

        Doctor doctor = doctorRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor not found"));

        List<Appointment> queue = doctorService.getQueueForDoctorByThree(doctor.getId());

        if (queue.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(queue);
    }

    @GetMapping("/queue/count")
    public ResponseEntity<?> getDailyCount(
            Authentication authentication
    ) {
        String email = authentication.getName();
        Optional<Doctor> doctorOpt = doctorRepository.findByEmail(email);

        if (doctorOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        }

        long totalPatients = doctorService.getDailyQueueCount(doctorOpt.get().getId(), LocalDateTime.now());

        return ResponseEntity.ok(totalPatients);
    }

    @PatchMapping("/queue/add-consultation/{appointmentId}")
    public ResponseEntity<?> addConsultation(
            Authentication authentication,
            @PathVariable UUID appointmentId
    ) {
        String email = authentication.getName();
        Optional<Doctor> doctorOpt = doctorRepository.findByEmail(email);

        if (doctorOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        }
        return ResponseEntity.ok(doctorService.updateAppointmentStatus(appointmentId, AppointmentStatus.IN_CONSULTATION));
    }

    @PatchMapping("/queue/add-referral/{appointmentId}")
    public ResponseEntity<?> addReferral(
            Authentication authentication,
            @PathVariable UUID appointmentId
    ) {
        String email = authentication.getName();
        Optional<Doctor> doctorOpt = doctorRepository.findByEmail(email);

        if (doctorOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        }
        return ResponseEntity.ok(doctorService.updateAppointmentStatus(appointmentId, AppointmentStatus.REFERRED));
    }

    @GetMapping("/chart/{appointmentId}")
    public ResponseEntity<?> getChart(
            Authentication authentication,
            @PathVariable UUID appointmentId
            ) {
        String email = authentication.getName();
        Optional<Doctor> doctorOpt = doctorRepository.findByEmail(email);

        if (doctorOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        }
        return ResponseEntity.ok(doctorService.getAppointmentById(appointmentId));
    }

    @PatchMapping("/chart/add-note/{appointmentId}")
    public ResponseEntity<?> addNote(
            @RequestBody String note,
            @PathVariable UUID appointmentId,
            Authentication authentication
    ) {
        String email = authentication.getName();
        Optional<Doctor> doctorOpt = doctorRepository.findByEmail(email);

        if (doctorOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        }
        return ResponseEntity.ok(doctorService.addNote(note, appointmentId));
    }

    @PostMapping("/chart/add-prescription")
    public ResponseEntity<?> addPrescription(
            @RequestBody Prescription prescription,
            Authentication authentication
    ) {
        String email = authentication.getName();
        Optional<Doctor> doctorOpt = doctorRepository.findByEmail(email);

        if (doctorOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        }
        return ResponseEntity.ok(doctorService.createPrescription(prescription));
    }

    @GetMapping("/payments")
    public ResponseEntity<?> getPayments(
            Authentication authentication
    ) {
        // Extract email from JWT token
        String email = authentication.getName();

        // Look up doctor by email
        Optional<Doctor> doctorOpt = doctorRepository.findByEmail(email);
        if (doctorOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        }

        return ResponseEntity.ok(doctorService.getPaymentsByTwo(email));
    }

    @GetMapping("/payments/todays")
    public ResponseEntity<?> getTodayPayments(
            Authentication authentication
    ) {
        // Extract email from JWT token
        String email = authentication.getName();

        // Look up doctor by email
        Optional<Doctor> doctorOpt = doctorRepository.findByEmail(email);
        if (doctorOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        }

        return ResponseEntity.ok(doctorService.getTotalPaymentToday(email, LocalDate.now()));
    }

    @GetMapping("/payments/last-week")
    public ResponseEntity<?> getLastWeekPayments(
        Authentication authentication
    ) {
        // Extract email from JWT token
        String email = authentication.getName();

        // Look up doctor by email
        Optional<Doctor> doctorOpt = doctorRepository.findByEmail(email);
        if (doctorOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        }

        return ResponseEntity.ok(doctorService.getLastWeekPayment(email, LocalDate.now().minusWeeks(1)));

    }

    @GetMapping("/payments/last-month")
    public ResponseEntity<?> getLastMonthPayments(
        Authentication authentication
    ) {

        // Extract email from JWT token
        String email = authentication.getName();

        // Look up doctor by email
        Optional<Doctor> doctorOpt = doctorRepository.findByEmail(email);
        if (doctorOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        }

        return ResponseEntity.ok(doctorService.getLastMonthPayment(email, LocalDate.now().minusMonths(1)));
    }

    @GetMapping("/payment/over-all")
    public ResponseEntity<?> getOverallPayments(
        Authentication authentication
    ) {
        // Extract email from JWT token
        String email = authentication.getName();

        // Look up doctor by email
        Optional<Doctor> doctorOpt = doctorRepository.findByEmail(email);
        if (doctorOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        }

        return ResponseEntity.ok(doctorService.getAllPayment(email));
    }

    @GetMapping("/payments/all-perMonth")
    public ResponseEntity<?> getAllPaymentPerMonth(
        Authentication authentication
    ) {
        // Extract email from JWT token
        String email = authentication.getName();

        // Look up doctor by email
        Optional<Doctor> doctorOpt = doctorRepository.findByEmail(email);
        if (doctorOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        }

        return ResponseEntity.ok(doctorService.getAllPaymentPerMonth(email));
    }

    @GetMapping("/consultations")
    public ResponseEntity<?> getConsultations(
            Authentication authentication
    ) {
        // Extract email from JWT token
        String email = authentication.getName();

        // Look up doctor by email
        Optional<Doctor> doctorOpt = doctorRepository.findByEmail(email);
        if (doctorOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        }

        return ResponseEntity.ok(doctorService.getAllConsultations(email));
    }

    @GetMapping("/consultations/get-total")
    public ResponseEntity<?> getConsultationTotal(
        Authentication authentication
    ) {
        // Extract email from JWT token
        String email = authentication.getName();

        // Look up doctor by email
        Optional<Doctor> doctorOpt = doctorRepository.findByEmail(email);
        if (doctorOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        }

        return ResponseEntity.ok(doctorService.getTotalConsultation(email));
    }

    @GetMapping("/consultations/get_byMonth")
    public ResponseEntity<?> getConsultationByMonth(
        Authentication authentication
    ) {
        // Extract email from JWT token
        String email = authentication.getName();

        // Look up doctor by email
        Optional<Doctor> doctorOpt = doctorRepository.findByEmail(email);
        if (doctorOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        }

        return ResponseEntity.ok(doctorService.getTotalConsultationByMonth(email, LocalDateTime.now().minusMonths(1)));
    }

    @GetMapping("/consultations/get-unique")
    public ResponseEntity<?> getConsultationUnique(
        Authentication authentication
    ) {
        // Extract email from JWT token
        String email = authentication.getName();

        // Look up doctor by email
        Optional<Doctor> doctorOpt = doctorRepository.findByEmail(email);
        if (doctorOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        }

        return ResponseEntity.ok(doctorService.getUniquePatients(email));
    }

    @GetMapping("/referrals/out-going")
    public ResponseEntity<?> getOutgoingReferrals(
        Authentication authentication
    ) {
        // Extract email from JWT token
        String email = authentication.getName();

        // Look up doctor by email
        Optional<Doctor> doctorOpt = doctorRepository.findByEmail(email);
        if (doctorOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        }

        return ResponseEntity.ok(doctorService.getOutGoingReferrals(email));
    }

    @GetMapping("/referrals/in-coming")
    public ResponseEntity<?> getInComingReferrals(
        Authentication authentication
    ) {
        // Extract email from JWT token
        String email = authentication.getName();

        // Look up doctor by email
        Optional<Doctor> doctorOpt = doctorRepository.findByEmail(email);
        if (doctorOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        }

        return ResponseEntity.ok(doctorService.getInComingReferrals(email));
    }

    @PatchMapping("/referrals/add-referral/{appointmentId}/{referedDoctorId}")
    public ResponseEntity<?> addReferral(
            Authentication authentication,
            @PathVariable UUID appointmentId,
            @PathVariable UUID referedDoctorId
    ) {
        // Extract email from JWT token
        String email = authentication.getName();

        // Look up doctor by email
        Optional<Doctor> doctorOpt = doctorRepository.findByEmail(email);
        if (doctorOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
        }

        return ResponseEntity.ok(doctorService.addReferral(appointmentId, referedDoctorId));
    }

}
