package com.healthlinkteam.healthlink.service;

import com.healthlinkteam.healthlink.dto.CreateMedicationDTO;
import com.healthlinkteam.healthlink.dto.MedicationDTO;
import com.healthlinkteam.healthlink.entity.Medication;
import com.healthlinkteam.healthlink.enums.MedicationStatus;
import com.healthlinkteam.healthlink.repository.MedicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicationService {

    private final MedicationRepository medicationRepository;

    // Get all medications
    public List<MedicationDTO> getAllMedications() {
        return medicationRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get medications by status
    public List<MedicationDTO> getByStatus(MedicationStatus status) {
        return medicationRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get medications for an appointment
    public List<MedicationDTO> getByAppointment(UUID appointmentId) {
        return medicationRepository.findByAppointmentId(appointmentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Create a new medication
    public MedicationDTO createMedication(CreateMedicationDTO dto) {
        Medication med = new Medication();
        med.setAppointmentId(dto.getAppointmentId());
        med.setMedicationName(dto.getMedicationName());
        med.setDosage(dto.getDosage());
        med.setDuration(dto.getDuration());
        med.setFrequency(dto.getFrequency());
        med.setSpecificInstructions(dto.getSpecificInstructions());
        med.setStatus(dto.getStatus() != null ? dto.getStatus() : MedicationStatus.PRESCRIBED);

        Medication saved = medicationRepository.save(med);
        return convertToDTO(saved);
    }

    // Update existing medication
    public MedicationDTO updateMedication(UUID id, CreateMedicationDTO dto) {
        Medication med = medicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medication not found"));

        med.setMedicationName(dto.getMedicationName());
        med.setDosage(dto.getDosage());
        med.setDuration(dto.getDuration());
        med.setFrequency(dto.getFrequency());
        med.setSpecificInstructions(dto.getSpecificInstructions());
        med.setStatus(dto.getStatus() != null ? dto.getStatus() : med.getStatus());

        Medication saved = medicationRepository.save(med);
        return convertToDTO(saved);
    }

    // Delete medication
    public void deleteMedication(UUID id) {
        medicationRepository.deleteById(id);
    }

    private MedicationDTO convertToDTO(Medication med) {
        MedicationDTO dto = new MedicationDTO();
        dto.setId(med.getId());
        dto.setAppointmentId(med.getAppointmentId());
        dto.setMedicationName(med.getMedicationName());
        dto.setDosage(med.getDosage());
        dto.setDuration(med.getDuration());
        dto.setFrequency(med.getFrequency());
        dto.setSpecificInstructions(med.getSpecificInstructions());
        dto.setStatus(med.getStatus());
        dto.setCreatedAt(med.getCreatedAt());
        return dto;
    }
}
