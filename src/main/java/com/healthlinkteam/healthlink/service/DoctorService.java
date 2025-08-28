package com.healthlinkteam.healthlink.service;

import com.healthlinkteam.healthlink.entity.*;
import com.healthlinkteam.healthlink.enums.AppointmentStatus;
import com.healthlinkteam.healthlink.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final UserRepository userRepository;

    public DoctorService(DoctorRepository doctorRepository, PasswordEncoder passwordEncoder, AppointmentRepository appointmentRepository, PrescriptionRepository prescriptionRepository, PaymentRepository paymentRepository, UserRepository userRepository) {
        this.doctorRepository = doctorRepository;
        this.passwordEncoder = passwordEncoder;
        this.appointmentRepository = appointmentRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
    }

    public List<Appointment> getQueueForDoctorByThree(UUID doctorId) {
        return appointmentRepository.findByDoctorIdByThree(doctorId);
    }

    public void removePatientFromQueue(UUID patientId) {
        appointmentRepository.deleteById(patientId);
    }

    public long getDailyQueueCount(UUID doctorId, LocalDateTime date) {
        return appointmentRepository.countByDoctorIdAndAppointmentDate(doctorId, date);
    }

    public boolean updateAppointmentStatus(UUID appointmentId, AppointmentStatus status) {
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
        if (appointment.isPresent()) {
            appointment.get().setStatus(status);
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

    public List<Appointment> getAllConsultations(String email) {
        User doctor = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("No doctor found"));
        return appointmentRepository.findAppointmentByDoctorIdAndStatus(doctor.getId(), AppointmentStatus.IN_CONSULTATION);
    }

    public long getTotalConsultation(String email) {
        return appointmentRepository.countAppointmentByStatusAndDoctorEmail(AppointmentStatus.IN_CONSULTATION,  email);
    }

    public long getTotalConsultationByMonth(String email, LocalDateTime date) {
        return appointmentRepository.countAppointmentByStatusAndDoctorEmailAndAppointmentDate(AppointmentStatus.IN_CONSULTATION, email, date);
    }

    public long getUniquePatients(String email) {
        return appointmentRepository.countUniqueAppointmentByDoctorEmailAndStatus(email, AppointmentStatus.IN_CONSULTATION);
    }

    public List<Appointment> getOutGoingReferrals(String email) {
        return appointmentRepository.findAppointmentByStatusAndDoctorEmail(AppointmentStatus.REFERRED, email);
    }

    public List<Appointment> getInComingReferrals(String email) {
        return appointmentRepository.findAppointmentByReferedDoctorEmailAndStatus(AppointmentStatus.REFERRED, email);
    }

    public boolean addReferral(UUID appointmentId, UUID doctorId) {
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
        Optional<Doctor> referedDoctor = doctorRepository.findById(doctorId);
        if(referedDoctor.isEmpty()) {
            throw new RuntimeException("Patient not found");
        }
        if (appointment.isPresent()) {
            appointment.get().setStatus(AppointmentStatus.REFERRED);
            appointment.get().setReferedDoctor(referedDoctor.get());
            return true;
        } else {
            throw new RuntimeException("Appointment not found");
        }
    }

}
