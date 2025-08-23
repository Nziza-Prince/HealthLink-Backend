package com.healthlinkteam.healthlink.service;

import com.healthlinkteam.healthlink.enums.StockStatus;
import com.healthlinkteam.healthlink.repository.DoctorRepository;
import com.healthlinkteam.healthlink.repository.FacilityRepository;
import com.healthlinkteam.healthlink.repository.MedicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MetricsService {
    private final VisitRequestRepository visitRequestRepo;
    private final DoctorRepository doctorRepo;
    private final MedicationRepository medicationRepo;
    private final FacilityRepository facilityRepo;


    public long totalVisitRequests() { return visitRequestRepo.count(); }
    public long totalStaff() { return doctorRepo.count(); }
    public long totalMedicalInventory() { return medicationRepo.count(); }
    public long totalFacilities() { return facilityRepo.count(); }


    public record PharmacySummary(long inStock, long pending, long lowStock, long collected) {}


    public PharmacySummary pharmacySummary() {
        return new PharmacySummary(
                medicationRepo.countByStatus(StockStatus.IN_STOCK),
                medicationRepo.countByStatus(StockStatus.PENDING),
                medicationRepo.countByStatus(StockStatus.LOW_STOCK),
                medicationRepo.countByStatus(StockStatus.COLLECTED)
        );
    }
}