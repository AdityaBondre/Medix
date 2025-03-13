package com.WhoKnows.Medix.model.DTOs;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class RescheduleAppointmentDTO {
    private LocalDate newDate;  // New appointment date provided by the doctor (format: YYYY-MM-DD)
    private LocalTime newTime;  // New appointment time provided by the doctor (format: HH:mm)

}