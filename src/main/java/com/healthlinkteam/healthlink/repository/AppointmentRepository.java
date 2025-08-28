package com.healthlinkteam.healthlink.repository;

import com.healthlinkteam.healthlink.entity.Appointment;
import com.healthlinkteam.healthlink.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    @Query("SELECT COUNT(a) FROM Appointment a")
    Long countTotalAppointments();

    List<Appointment> findAppointmentByStatus(AppointmentStatus appointmentStatus);

    /**
     * Find appointments by patient ID, ordered by appointment date descending
     */
    List<Appointment> findByPatientIdOrderByAppointmentDateDesc(UUID patientId);

    /**
     * Find appointments by doctor ID
     */
    @Query("SELECT a FROM Appointment a ORDER BY a.createdAt DESC LIMIT 3")
    List<Appointment> findByDoctorIdByThree(UUID doctorId);

    /**
     * Find appointments by department ID
     */
    // Search (by name or description)
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId " +
            "AND (LOWER(a.patient.firstName || a.patient.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(a.reason) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Appointment> searchPatients(Long doctorId, String keyword);

    long countByDoctorIdAndAppointmentDate(UUID doctorId, LocalDateTime appointmentDate);

    List<Appointment> findAppointmentByDoctorIdAndStatus(UUID email, AppointmentStatus status);

    long countAppointmentByStatusAndDoctorEmail(AppointmentStatus status, String email);

    long countAppointmentByStatusAndDoctorEmailAndAppointmentDate(AppointmentStatus status, String email, LocalDateTime serviceDate);

    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.doctor.email = :email AND a.status = :status")
    long countUniqueAppointmentByDoctorEmailAndStatus(String email,  AppointmentStatus status);

    List<Appointment> findAppointmentByStatusAndDoctorEmail(AppointmentStatus status, String email);

    List<Appointment> findAppointmentByReferedDoctorEmailAndStatus(AppointmentStatus status, String email);

    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate < :now AND a.status != 'COMPLETED'")
    List<Appointment> findDelayedAppointments(LocalDateTime now);

}
