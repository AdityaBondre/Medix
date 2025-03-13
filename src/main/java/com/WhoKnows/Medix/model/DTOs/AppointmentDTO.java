package com.WhoKnows.Medix.model.DTOs;

import com.WhoKnows.Medix.model.Appointment;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentDTO {
    private Long id;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String appointmentType;
    private String status;
    private String patientName;
    private String patientEmail;
    private Long patientId;
    private Long doctorId;
    private String doctorsName;
    // Getters and Setters

    // Mapping method
    public static AppointmentDTO mapToAppointmentDTO(Appointment appointment) {
        AppointmentDTO dto = new AppointmentDTO();
        dto.setId(appointment.getId());
        dto.setAppointmentDate(appointment.getAppointmentDate());
        dto.setAppointmentTime(appointment.getAppointmentTime());
        dto.setAppointmentType(appointment.getAppointmentType());
        dto.setStatus(appointment.getStatus());
        dto.setPatientName(appointment.getPatient().getName());
        dto.setPatientEmail(appointment.getPatient().getEmail());
        dto.setDoctorId(appointment.getDoctor().getId());
        dto.setPatientId(appointment.getPatient().getId());
        dto.setDoctorsName(appointment.getDoctor().getName());
        return dto;
    }
}