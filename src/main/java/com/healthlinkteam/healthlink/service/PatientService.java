package com.healthlinkteam.healthlink.service;

import com.healthlinkteam.healthlink.dto.PatientOverviewDto;
import com.healthlinkteam.healthlink.dto.PatientSignupDto;
import com.healthlinkteam.healthlink.entity.Department;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

/**
 * Service interface for patient-related business logic.
 * Defines all operations that can be performed on patient data.
 */
public interface PatientService {

    /**
     * Register a new patient
     */
    ResponseEntity<?> signup(PatientSignupDto signupDto);

    /**
     * Get patient overview/dashboard information
     */
    PatientOverviewDto getPatientOverview(UUID patientId);

    /**
     * Get all appointments for a patient
     */
    ResponseEntity<?> getPatientAppointments(UUID patientId);

    /**
     * Create a new appointment for a patient
     */
    ResponseEntity<?> createAppointment(UUID patientId, String departmentName, String reason, String preferredDate);

    /**
     * Get all prescriptions for a patient
     */
    ResponseEntity<?> getPatientPrescriptions(UUID patientId);

    /**
     * Get patient wallet information
     */
    ResponseEntity<?> getPatientWallet(UUID patientId);

    /**
     * Top up patient wallet
     */
    ResponseEntity<?> topUpWallet(UUID patientId, String amount, String paymentMethod);

    /**
     * Get all payments for a patient
     */
    ResponseEntity<?> getPatientPayments(UUID patientId);

    /**
     * Get all transactions for a patient
     */
    ResponseEntity<?> getPatientTransactions(UUID patientId);

    /**
     * Get patient profile information
     */
    ResponseEntity<?> getPatientProfile(UUID patientId);

    /**
     * Update patient profile information
     */
    ResponseEntity<?> updatePatientProfile(UUID patientId, PatientSignupDto profileDto);

    List<Department> getDepartments();
} 