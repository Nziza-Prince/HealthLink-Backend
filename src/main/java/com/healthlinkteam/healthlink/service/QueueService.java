package com.healthlinkteam.healthlink.service;

import com.healthlinkteam.healthlink.entity.Appointment;
import com.healthlinkteam.healthlink.entity.Prescription;
import com.healthlinkteam.healthlink.enums.AppointmentStatus;
import com.healthlinkteam.healthlink.repository.AppointmentRepository;
import com.healthlinkteam.healthlink.repository.PrescriptionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class QueueService {
    private final AppointmentRepository appointmentRepository;
    private final PrescriptionRepository prescriptionRepository;

    public QueueService(AppointmentRepository appointmentRepository, PrescriptionRepository prescriptionRepository) {
        this.appointmentRepository = appointmentRepository;
        this.prescriptionRepository = prescriptionRepository;
    }

    public List<Appointment> getQueueForDoctorByThree(UUID doctorId) {
        return appointmentRepository.findByDoctorIdByThree(doctorId);
    }

    public void removePatientFromQueue(UUID patientId) {
        appointmentRepository.deleteById(patientId);
    }

    public long getDailyQueueCount(UUID doctorId, LocalDate date) {
        return appointmentRepository.countByDoctorIdAndServiceDate(doctorId, date);
    }

    public boolean updateAppointmentStatus(UUID appointmentId) {
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
        if (appointment.isPresent()) {
            appointment.get().setStatus(AppointmentStatus.IN_CONSULTATION);
            return true;
        } else {
            return false;
        }
    }

    public Appointment getAppointmentById(UUID appointmentId) {
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
        if (appointment.isPresent()) {
            return appointment.get();
        } else {
            return null;
        }
    }

    public boolean addNote(String note, UUID appointmentId) {
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
        if (appointment.isPresent()) {
            appointment.get().setNotes(note);
            return true;
        } else {
            return false;
        }
    }

    public Prescription createPrescription(Prescription prescription) {
        return prescriptionRepository.
    }
}
