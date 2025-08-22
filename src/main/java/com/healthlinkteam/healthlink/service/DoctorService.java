package com.healthlinkteam.healthlink.service;

import com.healthlinkteam.healthlink.dto.RegisterDoctor;
import com.healthlinkteam.healthlink.entity.Appointment;
import com.healthlinkteam.healthlink.entity.Doctor;
import com.healthlinkteam.healthlink.entity.Payment;
import com.healthlinkteam.healthlink.entity.Prescription;
import com.healthlinkteam.healthlink.enums.AppointmentStatus;
import com.healthlinkteam.healthlink.repository.AppointmentRepository;
import com.healthlinkteam.healthlink.repository.DoctorRepository;
import com.healthlinkteam.healthlink.repository.PaymentRepository;
import com.healthlinkteam.healthlink.repository.PrescriptionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@Transactional
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppointmentRepository appointmentRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final PaymentRepository paymentRepository;

    public DoctorService(DoctorRepository doctorRepository, PasswordEncoder passwordEncoder, AppointmentRepository appointmentRepository, PrescriptionRepository prescriptionRepository, PaymentRepository paymentRepository) {
        this.doctorRepository = doctorRepository;
        this.passwordEncoder = passwordEncoder;
        this.appointmentRepository = appointmentRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.paymentRepository = paymentRepository;
    }

    public ResponseEntity<?> signup(RegisterDoctor signupDto) {
        try {
            // Check if passwords match
            if (!signupDto.getPassword().equals(signupDto.getConfirmPassword())) {
                return ResponseEntity.badRequest().body("Passwords do not match");
            }

            // Check if email already exists
            if (doctorRepository.existsByEmail(signupDto.getEmail())) {
                return ResponseEntity.badRequest().body("Email already registered");
            }

            // Create new patient
            Doctor doctor = new Doctor();
            doctor.setFirstName(signupDto.getFirstName());
            doctor.setLastName(signupDto.getLastName());
            doctor.setEmail(signupDto.getEmail());
            doctor.setCountryOfResidence(signupDto.getCountryResidence());
            doctor.setPhoneNumber(signupDto.getPhoneNumber());
            doctor.setPasswordHash(passwordEncoder.encode(signupDto.getPassword()));

            // Save doctor (this will save both User and Patient records due to JOINED inheritance)
            Doctor savedDoctor = doctorRepository.save(doctor);

            log.info("Doctor registered successfully: {}", savedDoctor.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body("Doctor registered successfully");

        } catch (Exception e) {
            log.error("Error during doctor signup: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed");
        }
    }

    public List<Appointment> getQueueForDoctorByThree(UUID doctorId) {
        return appointmentRepository.findByDoctorIdByThree(doctorId);
    }

    public void removePatientFromQueue(UUID patientId) {
        appointmentRepository.deleteById(patientId);
    }

    public long getDailyQueueCount(UUID doctorId, LocalDate date) {
        return appointmentRepository.countByDoctorIdAndServiceDate(doctorId, date);
    }

    public boolean updateAppointmentStatus(UUID appointmentId) {
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
        if (appointment.isPresent()) {
            appointment.get().setStatus(AppointmentStatus.IN_CONSULTATION);
            return true;
        } else {
            return false;
        }
    }

    public Appointment getAppointmentById(UUID appointmentId) {
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
        if (appointment.isPresent()) {
            return appointment.get();
        } else {
            return null;
        }
    }

    public boolean addNote(String note, UUID appointmentId) {
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
        if (appointment.isPresent()) {
            appointment.get().setNotes(note);
            return true;
        } else {
            return false;
        }
    }

    public Prescription createPrescription(Prescription prescription) {
        return prescriptionRepository.save(prescription);
    }

    public List<Payment> getPaymentsByTwo(String email) {
        return paymentRepository.findByDoctorEmailOrderByCreatedAtDescByTwo(email);
    }

    public long getTotalPaymentToday(String email, LocalDate date) {
        return paymentRepository.countAmountByDoctorEmailAndServiceDate(email, date);
    }

    public long getLastWeekPayment(String email, LocalDate date) {
        return paymentRepository.countAmountByDoctorEmailAndServiceDate(email, date);
    }

    public long getLastMonthPayment(String email, LocalDate date) {
        return paymentRepository.countAmountByDoctorEmailAndServiceDate(email, date);
    }

    public long getAllPayment(String email) {
        return paymentRepository.countAmountByDoctorEmail(email);
    }

    public List<Long> getAllPaymentPerMonth(String email) {
        return paymentRepository.findAmountPerMonthByDoctorEmail(email);
    }

}
