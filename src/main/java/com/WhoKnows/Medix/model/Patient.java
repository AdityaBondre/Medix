package com.WhoKnows.Medix.model;

import jakarta.persistence.Entity;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Patient extends User{

    private LocalDate dateOfBirth;
    private String bloodGroup;


    // Getters and Setters
}