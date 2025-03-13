package com.WhoKnows.Medix.model;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Doctor extends  User {
    private LocalDate dateOfBirth;
    private String specialization;
    private String licenseNumber;
    private String hospitalName;
    // Approval Status: PENDING, APPROVED, REJECTED
    private String status = "PENDING";

    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_APPROVED = "APPROVED";
    public static final String STATUS_REJECTED = "REJECTED";

    // Getters and Setters
}