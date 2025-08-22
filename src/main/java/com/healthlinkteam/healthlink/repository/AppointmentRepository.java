package com.healthlinkteam.healthlink.repository;

import com.healthlinkteam.healthlink.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AppointmentRepository extends JpaRepository<Appointment, Long> { }