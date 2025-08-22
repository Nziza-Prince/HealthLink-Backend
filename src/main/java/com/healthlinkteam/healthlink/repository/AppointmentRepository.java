package com.healthlinkteam.healthlink.repository;

import com.healthlinkteam.healthlink.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for Appointment entity.
 * Provides data access methods for appointment operations.
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    /**
     * Find appointments by patient ID, ordered by appointment date descending
     */
    List<Appointment> findByPatientIdOrderByAppointmentDateDesc(UUID patientId);

    /**
     * Find appointments by doctor ID
     */
    List<Appointment> findByDoctorId(UUID doctorId);

    /**
     * Find appointments by department ID
     */
    List<Appointment> findByDepartmentId(UUID departmentId);

    /**
     * Find appointments by hospital ID
     */
    List<Appointment> findByHospitalId(UUID hospitalId);
} 