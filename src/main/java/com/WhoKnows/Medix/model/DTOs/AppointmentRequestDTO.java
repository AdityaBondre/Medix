package com.WhoKnows.Medix.model.DTOs;



import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
public class AppointmentRequestDTO {

    @NonNull
    private Long doctorId; // The ID of the doctor the patient is requesting an appointment with

    @NonNull
    private LocalDate appointmentDate; // The date the patient wants the appointment

    private String appointmentType;

    private String notes; // Optional notes or reason for the appointment
}
