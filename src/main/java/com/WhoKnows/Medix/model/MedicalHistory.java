package com.WhoKnows.Medix.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class MedicalHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Patient patient;
    @ManyToOne
    private Doctor doctor;
    @ManyToOne
    private  Appointment appointment;
    @Column(columnDefinition = "LONGTEXT")
    private String summary;
}
