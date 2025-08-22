package com.healthlinkteam.healthlink.repository;

import com.healthlinkteam.healthlink.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DoctorRepository extends JpaRepository<Doctor, Long> { }