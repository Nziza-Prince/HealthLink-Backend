package com.healthlinkteam.healthlink.service;

import com.healthlinkteam.healthlink.dto.AssignDoctorDTO;
import com.healthlinkteam.healthlink.dto.CreateAppointmentDto;
import com.healthlinkteam.healthlink.entity.Appointment;
import com.healthlinkteam.healthlink.entity.User;
import com.healthlinkteam.healthlink.enums.AppointmentStatus;
import com.healthlinkteam.healthlink.enums.NotificationType;
import com.healthlinkteam.healthlink.repository.AppointmentRepository;
import com.healthlinkteam.healthlink.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QueueService {

    @Autowired
    private AppointmentRepository visitRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    public List<CreateAppointmentDto> getUnassignedQueue() {
        return visitRequestRepository.findByStatus(AppointmentStatus.UNASSIGNED).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CreateAppointmentDto> getAllQueue() {
        return visitRequestRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void assignDoctor(AssignDoctorDTO assignDTO) {
        Appointment visitRequest = visitRequestRepository.findById(assignDTO.getVisitRequestId())
                .orElseThrow(() -> new RuntimeException("Visit request not found"));

        User doctor = userRepository.findById(assignDTO.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        visitRequest.setAssignedDoctor(doctor);
        visitRequest.setStatus(AppointmentStatus.ASSIGNED);

        visitRequestRepository.save(visitRequest);

        // Send notification to doctor
        notificationService.sendNotification(
                doctor.getId(),
                "New patient assigned: " + visitRequest.getPatient().getName(),
                NotificationType.GENERAL
        );
    }

    public List<CreateAppointmentDto> getDelayedAppointments() {
        return visitRequestRepository.findDelayedAppointments(LocalDateTime.now()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private CreateAppointmentDto convertToDTO(Appointment visit) {
        CreateAppointmentDto dto = new CreateAppointmentDto();
        dto.setDepartmentName(visit.getDepartment().getName());
        dto.setReason(visit.getReason());
        dto.setPreferredDate(visit.getAppointmentDate().toString());
        dto.setPatientId(String.valueOf(visit.getPatient().getId()));
        return dto;
    }


}