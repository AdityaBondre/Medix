package com.WhoKnows.Medix.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Patient patient;

    @ManyToOne
    private Doctor doctor;

    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String status;  // Pending, Approved, Rejected, Cancelled, Rescheduled
    private String appointmentType;  // Physical or Virtual
    private String videoCallUrl;


    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_APPROVED = "APPROVED";
    public static final String STATUS_REJECTED = "REJECTED";
    public static final String STATUS_CANCELLED = "CANCELLED";
    public static final String STATUS_RESCHEDULED = "RESCHEDULED";
    public static final String TYPE_PHYSICAL = "PHYSICAL";
    public static final String TYPE_VIRTUAL = "VIRTUAL";

    // Getters and Setters
}
