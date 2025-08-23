package com.healthlinkteam.healthlink.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AssignDoctorDTO {
    private Long visitRequestId;
    private Long doctorId;
    private String doctorName;
    private String department;
    private LocalDate assignmentDate;
}