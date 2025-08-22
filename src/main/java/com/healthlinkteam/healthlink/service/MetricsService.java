package com.healthlinkteam.healthlink.service;

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
                medicationRepo.countByStatus(InventoryStatus.IN_STOCK),
                medicationRepo.countByStatus(InventoryStatus.PENDING),
                medicationRepo.countByStatus(InventoryStatus.LOW_STOCK),
                medicationRepo.countByStatus(InventoryStatus.COLLECTED)
        );
    }
}