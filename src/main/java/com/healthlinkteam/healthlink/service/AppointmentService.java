package com.healthlinkteam.healthlink.service;

import com.healthlinkteam.healthlink.dto.AppointmentDTO;
import com.healthlinkteam.healthlink.dto.AssignAppointmentDTO;
import com.healthlinkteam.healthlink.dto.CreateAppointmentDto;
import com.healthlinkteam.healthlink.entity.Appointment;
import com.healthlinkteam.healthlink.enums.AppointmentStatus;
import com.healthlinkteam.healthlink.enums.NotificationType;
import com.healthlinkteam.healthlink.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final NotificationService notificationService;

    public AppointmentDTO createAppointment(CreateAppointmentDto dto) {
        Appointment appointment = new Appointment();
        appointment.setPatientId(UUID.fromString(dto.getPatientId()));
        appointment.setDepartment(dto.getDepartmentName());
        appointment.setReason(dto.getReason());
        appointment.setAppointmentDate(LocalDateTime.parse(dto.getPreferredDate() + "T09:00:00"));
        appointment.setStatus(AppointmentStatus.UNASSIGNED);

        Appointment saved = appointmentRepository.save(appointment);
        return convertToDTO(saved);
    }

    public List<AppointmentDTO> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Long getTotalAppointmentRequests() {
        return appointmentRepository.countTotalAppointments();
    }

    public Long getUnassignedAppointmentsCount() {
        return appointmentRepository.countUnassignedAppointments();
    }

    public List<AppointmentDTO> getDelayedAppointments() {
        return appointmentRepository.findDelayedAppointments(LocalDateTime.now())
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public AppointmentDTO assignAppointment(UUID appointmentId, AssignAppointmentDTO assignDto) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setDoctorId(assignDto.getDoctorId());
        appointment.setAppointmentDate(assignDto.getAppointmentDate());
        appointment.setDepartment(assignDto.getDepartment());
        appointment.setStatus(AppointmentStatus.ASSIGNED);

        Appointment updated = appointmentRepository.save(appointment);

        // Notify the patient
        notificationService.sendNotification(
                appointment.getPatientId(),
                assignDto.getDescription(),
                NotificationType.APPOINTMENT_ASSIGNED
        );

        return convertToDTO(updated);
    }

    private AppointmentDTO convertToDTO(Appointment appointment) {
        AppointmentDTO dto = new AppointmentDTO();
        dto.setId(appointment.getId());
        dto.setPatientId(appointment.getPatientId());
        dto.setDoctorId(appointment.getDoctorId());
        dto.setDepartment(appointment.getDepartment());
        dto.setHospital(appointment.getHospital());
        dto.setReason(appointment.getReason());
        dto.setDiagnosis(appointment.getDiagnosis());
        dto.setAppointmentDate(appointment.getAppointmentDate());
        dto.setDurationMinutes(appointment.getDurationMinutes());
        dto.setStatus(appointment.getStatus());
        dto.setQueuePosition(appointment.getQueuePosition());
        dto.setAmountPaid(appointment.getAmountPaid());
        dto.setNotes(appointment.getNotes());
        dto.setCreatedAt(appointment.getCreatedAt());
        dto.setUpdatedAt(appointment.getUpdatedAt());
        return dto;
    }
}
