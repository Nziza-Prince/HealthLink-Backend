package com.healthlinkteam.healthlink.repository;

import com.healthlinkteam.healthlink.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    /**
     * Find payments by patient ID, ordered by creation date descending
     */
    List<Payment> findByPatientIdOrderByCreatedAtDesc(UUID patientId);

    /**
     * Find payments by appointment ID
     */
    List<Payment> findByAppointmentId(UUID appointmentId);

    /**
     * Find payments by status
     */
    List<Payment> findByStatus(String status);

    @Query("SELECT p from Payment p WHERE p.appointment.doctor.email = :email ORDER BY p.createdAt DESC LIMIT 3")
    List<Payment> findByDoctorEmailOrderByCreatedAtDescByTwo(String email);

    @Query("SELECT sum(p.amount) FROM Payment p WHERE p.appointment.doctor.email = :email AND p.createdAt = :serviceDate")
    long countAmountByDoctorEmailAndServiceDate(String email, LocalDate serviceDate);

    @Query("SELECT sum(p.amount) FROM Payment p WHERE p.appointment.doctor.email = :email")
    long countAmountByDoctorEmail(String email);

    @Query("SELECT sum(p.amount) FROM Payment p WHERE p.appointment.doctor.email = :email GROUP BY DATE_TRUNC('month', p.dueDate) ORDER BY month")
    List<Long> findAmountPerMonthByDoctorEmail(String email);
}
