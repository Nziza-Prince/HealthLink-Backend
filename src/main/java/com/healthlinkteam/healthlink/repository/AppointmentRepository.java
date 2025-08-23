package com.healthlinkteam.healthlink.repository;

import com.healthlinkteam.healthlink.dto.CreateAppointmentDto;
import com.healthlinkteam.healthlink.entity.Appointment;
import com.healthlinkteam.healthlink.entity.User;
import com.healthlinkteam.healthlink.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<CreateAppointmentDto> findByStatus(AppointmentStatus status);
    List<CreateAppointmentDto> findByAssignedDoctor(User doctor);
    List<CreateAppointmentDto> findByServiceDateBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT COUNT(v) FROM CreateAppointmentDto v")
    Long countTotalVisitRequests();

    @Query("SELECT COUNT(v) FROM CreateAppointmentDto v WHERE v.status = 'UNASSIGNED'")
    Long countUnassignedRequests();

    @Query("SELECT v FROM CreateAppointmentDto v WHERE v.serviceDate < :now AND v.status != 'COMPLETED'")
    List<CreateAppointmentDto> findDelayedAppointments(LocalDateTime now);
}