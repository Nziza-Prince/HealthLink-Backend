package com.healthlinkteam.healthlink.controller;

import com.healthlinkteam.healthlink.entity.Appointment;
import com.healthlinkteam.healthlink.entity.Doctor;
import com.healthlinkteam.healthlink.entity.Prescription;
import com.healthlinkteam.healthlink.repository.DoctorRepository;
import com.healthlinkteam.healthlink.service.DoctorService;
import com.healthlinkteam.healthlink.service.QueueService;
import com.healthlinkteam.healthlink.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/doctor")
public class DoctorController {

    private final QueueService queueService;
    private final JwtUtil jwtUtil;
    private final DoctorRepository doctorRepository;
    private final DoctorService doctorService;

    public DoctorController(QueueService queueService, JwtUtil jwtUtil, DoctorRepository doctorRepository, DoctorService doctorService) {
        this.queueService = queueService;
        this.jwtUtil = jwtUtil;
        this.doctorRepository = doctorRepository;
        this.doctorService = doctorService;
    }

    // Get all patients in a doctor's queue
    @GetMapping("/queue")
    public ResponseEntity<?> getQueue(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization header required");
        }

        String token = authHeader.substring(7); // remove "Bearer "
        try {
            // Extract email from JWT token
            String email = jwtUtil.extractUsername(token);

            // Look up doctor by email
            Optional<Doctor> doctorOpt = doctorRepository.findByEmail(email);
            if (doctorOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
            }

            Doctor doctor = doctorOpt.get();
            List<Appointment> queue = queueService.getQueueForDoctorByThree(doctor.getId());

            if (queue.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(queue);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
    }

    @GetMapping("/queue/count")
    public ResponseEntity<?> getDailyCount(
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization header required");
        }

        String token = authHeader.substring(7);
        try {
            String email = jwtUtil.extractUsername(token);
            Optional<Doctor> doctorOpt = doctorRepository.findByEmail(email);

            if (doctorOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
            }

            long totalPatients = queueService.getDailyQueueCount(doctorOpt.get().getId(), LocalDate.now());

            return ResponseEntity.ok(totalPatients);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
    }

    @PatchMapping("/queue/add-consultation/{appointmentId}")
    public ResponseEntity<?> addConsultation(
            @PathVariable UUID appointmentId
    ) {
        return ResponseEntity.ok(queueService.updateAppointmentStatus(appointmentId));
    }

    @GetMapping("/chart/{appointmentId}")
    public ResponseEntity<?> getChart(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @PathVariable UUID appointmentId
            ) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization header required");
        }
        return ResponseEntity.ok(queueService.getAppointmentById(appointmentId));
    }

    @PatchMapping("/chart/add-note/{appointmentId}")
    public ResponseEntity<?> addNote(
            @RequestBody String note,
            @PathVariable UUID appointmentId
    ) {
        return ResponseEntity.ok(queueService.addNote(note, appointmentId));
    }

    @PostMapping("/chart/add-prescription")
    public ResponseEntity<?> addPrescription(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody Prescription prescription
    ) {
        return ResponseEntity.ok(queueService.createPrescription(prescription));
    }

    @GetMapping("/payments")
    public ResponseEntity<?> getPayments(
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization header required");
        }

        String token = authHeader.substring(7); // remove "Bearer "
        try {
            // Extract email from JWT token
            String email = jwtUtil.extractUsername(token);

            // Look up doctor by email
            Optional<Doctor> doctorOpt = doctorRepository.findByEmail(email);
            if (doctorOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
            }

            return ResponseEntity.ok(doctorService.getPaymentsByTwo(email));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
    }

    @GetMapping("/payments/todays")
    public ResponseEntity<?> getTodayPayments(
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization header required");
        }

        String token = authHeader.substring(7); // remove "Bearer "
        try {
            // Extract email from JWT token
            String email = jwtUtil.extractUsername(token);

            // Look up doctor by email
            Optional<Doctor> doctorOpt = doctorRepository.findByEmail(email);
            if (doctorOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
            }

            return ResponseEntity.ok(doctorService.getTotalPaymentToday(email, LocalDate.now()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
    }

    @GetMapping("/payments/last-week")
    public ResponseEntity<?> getLastWeekPayments(
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization header required");
        }

        String token = authHeader.substring(7); // remove "Bearer "
        try {
            // Extract email from JWT token
            String email = jwtUtil.extractUsername(token);

            // Look up doctor by email
            Optional<Doctor> doctorOpt = doctorRepository.findByEmail(email);
            if (doctorOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
            }

            return ResponseEntity.ok(doctorService.getLastWeekPayment(email, LocalDate.now().minusWeeks(1)));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
    }

    @GetMapping("/payments/last-month")
    public ResponseEntity<?> getLastMonthPayments(
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization header required");
        }

        String token = authHeader.substring(7); // remove "Bearer "
        try {
            // Extract email from JWT token
            String email = jwtUtil.extractUsername(token);

            // Look up doctor by email
            Optional<Doctor> doctorOpt = doctorRepository.findByEmail(email);
            if (doctorOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
            }

            return ResponseEntity.ok(doctorService.getLastMonthPayment(email, LocalDate.now().minusMonths(1)));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
    }

    @GetMapping("/payment/over-all")
    public ResponseEntity<?> getOverallPayments(
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization header required");
        }

        String token = authHeader.substring(7); // remove "Bearer "
        try {
            // Extract email from JWT token
            String email = jwtUtil.extractUsername(token);

            // Look up doctor by email
            Optional<Doctor> doctorOpt = doctorRepository.findByEmail(email);
            if (doctorOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
            }

            return ResponseEntity.ok(doctorService.getAllPayment(email));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
    }

    @GetMapping("/payments/all-perMonth")
    public ResponseEntity<?> getAllPaymentPerMonth(
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization header required");
        }

        String token = authHeader.substring(7); // remove "Bearer "
        try {
            // Extract email from JWT token
            String email = jwtUtil.extractUsername(token);

            // Look up doctor by email
            Optional<Doctor> doctorOpt = doctorRepository.findByEmail(email);
            if (doctorOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found");
            }

            return ResponseEntity.ok(doctorService.getAllPaymentPerMonth(email));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
    }

}
