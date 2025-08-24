package com.healthlinkteam.healthlink.repository;

import com.healthlinkteam.healthlink.entity.Appointment;
import com.healthlinkteam.healthlink.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    List<Appointment> findByStatus(AppointmentStatus status);
    List<Appointment> findByDoctorId(UUID doctorId);
    List<Appointment> findByAppointmentDateBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT COUNT(a) FROM Appointment a")
    Long countTotalAppointments();

    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.status = 'UNASSIGNED'")
    Long countUnassignedAppointments();

    @Query("SELECT a FROM Appointment a WHERE a.appointmentDate < :now AND a.status != 'COMPLETED'")
    List<Appointment> findDelayedAppointments(LocalDateTime now);
}
