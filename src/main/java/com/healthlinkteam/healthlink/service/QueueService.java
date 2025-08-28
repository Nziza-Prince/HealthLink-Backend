package com.healthlinkteam.healthlink.service;

import com.healthlinkteam.healthlink.dto.AssignAppointmentDTO;
import com.healthlinkteam.healthlink.dto.CreateAppointmentDto;
import com.healthlinkteam.healthlink.entity.Appointment;
import com.healthlinkteam.healthlink.entity.Doctor;
import com.healthlinkteam.healthlink.entity.User;
import com.healthlinkteam.healthlink.enums.AppointmentStatus;
import com.healthlinkteam.healthlink.enums.NotificationType;
import com.healthlinkteam.healthlink.repository.AppointmentRepository;
import com.healthlinkteam.healthlink.repository.PrescriptionRepository;
import com.healthlinkteam.healthlink.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class QueueService {
    private final AppointmentRepository appointmentRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public QueueService(AppointmentRepository appointmentRepository, PrescriptionRepository prescriptionRepository, UserRepository userRepository, NotificationService notificationService) {
        this.appointmentRepository = appointmentRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    public List<CreateAppointmentDto> getUnassignedQueue() {
        return appointmentRepository.findAppointmentByStatus(AppointmentStatus.SCHEDULED).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CreateAppointmentDto> getAllQueue() {
        return appointmentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void assignDoctor(AssignAppointmentDTO assignDTO) {
        Appointment appointment = appointmentRepository.findById(assignDTO.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        User doctor = userRepository.findById(assignDTO.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        appointment.setDoctor((Doctor) doctor);
        appointment.setStatus(AppointmentStatus.IN_QUEUE);

        appointmentRepository.save(appointment);

        // Fetch patient name
        User patient = userRepository.findById(appointment.getPatient().getId())
                .orElse(null);

        String patientName = (patient != null) ? patient.getFirstName() + patient.getLastName() : "Unknown Patient";

        // Send notification to doctor
        notificationService.sendNotification(
                doctor.getId(),
                "New patient assigned: " + patientName,
                NotificationType.GENERAL
        );
    }


    public List<CreateAppointmentDto> getDelayedAppointments() {
        return appointmentRepository.findDelayedAppointments(LocalDateTime.now()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private CreateAppointmentDto convertToDTO(Appointment visit) {
        CreateAppointmentDto dto = new CreateAppointmentDto();
        dto.setDepartmentName(visit.getDoctor().getDepartment().getName());
        dto.setReason(visit.getReason());
        dto.setPreferredDate(visit.getAppointmentDate().toString());
        dto.setPatientId(String.valueOf(visit.getPatient().getId())); // keep UUID, not String
        return dto;
    }
}
