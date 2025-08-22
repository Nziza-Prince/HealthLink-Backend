package com.healthlinkteam.healthlink.repository;

import com.healthlinkteam.healthlink.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    /**
     * Find appointments by patient ID, ordered by appointment date descending
     */
    List<Appointment> findByPatientIdOrderByAppointmentDateDesc(UUID patientId);

    /**
     * Find appointments by doctor ID
     */
    @Query("SELECT a FROM Appointment a ORDER BY a.createdAt LIMIT 3")
    List<Appointment> findByDoctorIdByThree(UUID doctorId);

    /**
     * Find appointments by department ID
     */
    List<Appointment> findByDepartmentId(UUID departmentId);

    /**
     * Find appointments by hospital ID
     */
    List<Appointment> findByHospitalId(UUID hospitalId);

    // Filter by doctor + serviceDate
    List<Appointment> findByDoctorIdAndServiceDateOrderByServiceDateAsc(Long doctorId, LocalDate serviceDate);

    // Search (by name or description)
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId " +
            "AND (LOWER(a.patient.firstName || a.patient.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(a.reason) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Appointment> searchPatients(Long doctorId, String keyword);

    long countByDoctorIdAndServiceDate(UUID doctorId, LocalDate serviceDate);
}
