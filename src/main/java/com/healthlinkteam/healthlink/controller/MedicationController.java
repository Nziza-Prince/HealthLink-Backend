package com.healthlinkteam.healthlink.controller;

import com.healthlinkteam.healthlink.dto.CreateMedicationDTO;
import com.healthlinkteam.healthlink.dto.MedicationDTO;
import com.healthlinkteam.healthlink.enums.MedicationStatus;
import com.healthlinkteam.healthlink.service.MedicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/medications")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class MedicationController {

    private final MedicationService medicationService;

    @GetMapping
    public ResponseEntity<List<MedicationDTO>> getAll() {
        return ResponseEntity.ok(medicationService.getAllMedications());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<MedicationDTO>> getByStatus(@PathVariable MedicationStatus status) {
        return ResponseEntity.ok(medicationService.getByStatus(status));
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<List<MedicationDTO>> getByAppointment(@PathVariable UUID appointmentId) {
        return ResponseEntity.ok(medicationService.getByAppointment(appointmentId));
    }

    @PostMapping
    public ResponseEntity<MedicationDTO> create(@RequestBody CreateMedicationDTO dto) {
        return ResponseEntity.ok(medicationService.createMedication(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicationDTO> update(@PathVariable UUID id, @RequestBody CreateMedicationDTO dto) {
        return ResponseEntity.ok(medicationService.updateMedication(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        medicationService.deleteMedication(id);
        return ResponseEntity.ok("Medication deleted successfully");
    }
}
