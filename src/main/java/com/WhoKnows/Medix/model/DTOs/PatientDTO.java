package com.WhoKnows.Medix.model.DTOs;

import com.WhoKnows.Medix.model.Patient;
import lombok.Data;

import java.time.LocalDate;
@Data
public class PatientDTO {
    private String gender;
    private String name;
    private String email;
    private LocalDate dateOfBirth;
    private String bloodGroup;


    public static PatientDTO mapToPatientDTO(Patient patient){
        PatientDTO dto = new PatientDTO();

        dto.setName(patient.getName());
        dto.setEmail(patient.getEmail());
        dto.setGender(patient.getGender());
        dto.setBloodGroup(patient.getBloodGroup());
        dto.setDateOfBirth(patient.getDateOfBirth());

        return dto;
    }

}
