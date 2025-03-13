package com.WhoKnows.Medix.model.DTOs;

import com.WhoKnows.Medix.model.Doctor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DoctorDTO {
    private Long id;
    private String name;
    private String specialization;
    private String hospitalName;
    private String licenseNumber;
    private String gender;
    private LocalDateTime updatedAt;
    // Mapping method
    public static DoctorDTO mapToDoctorDTO(Doctor doctor) {
        DoctorDTO dto = new DoctorDTO();
        dto.setId(doctor.getId());
        dto.setName(doctor.getName());
        dto.setSpecialization(doctor.getSpecialization());
        dto.setHospitalName(doctor.getHospitalName());
        dto.setGender(doctor.getGender());
        dto.setLicenseNumber(doctor.getLicenseNumber());
        dto.setUpdatedAt(doctor.getUpdatedAt());
        return dto;
    }
}
