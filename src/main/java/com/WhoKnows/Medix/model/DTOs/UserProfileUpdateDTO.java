package com.WhoKnows.Medix.model.DTOs;

import lombok.Data;

import java.time.LocalDate;
@Data
public class UserProfileUpdateDTO {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String gender;

    // For Patient-specific attributes
    private String bloodGroup;
    private LocalDate dateOfBirth;

    // For Doctor-specific attributes
    private String specialization;
    private String licenseNumber;
    private String hospitalName;
    private String status;

    // Getters and Setters
}
