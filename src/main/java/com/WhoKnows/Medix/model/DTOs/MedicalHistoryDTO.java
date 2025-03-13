
package com.WhoKnows.Medix.model.DTOs;

import com.WhoKnows.Medix.model.MedicalHistory;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MedicalHistoryDTO {
    private Long id;
    private String summary;
    private Long patientId;
    private Long doctorId;
    private String doctorName;
    private Long appointmentId;
    private LocalDate appointmentDate;

    public static MedicalHistoryDTO mapToMedicalHistoryDTO(MedicalHistory medicalHistory) {
        MedicalHistoryDTO dto = new MedicalHistoryDTO(); // Corrected instance creation
        dto.setId(medicalHistory.getId());
        dto.setSummary(medicalHistory.getSummary());
        dto.setPatientId(medicalHistory.getPatient().getId()); // Assuming MedicalHistory has a patient reference
        dto.setDoctorId(medicalHistory.getDoctor().getId()); // Assuming MedicalHistory has a doctor reference
        dto.setDoctorName(medicalHistory.getDoctor().getName()); // Assuming doctor has a name field
        dto.setAppointmentId(medicalHistory.getAppointment().getId()); // Assuming MedicalHistory has an appointment reference
        dto.setAppointmentDate(medicalHistory.getAppointment().getAppointmentDate()); // Assuming appointment has a date field

        return dto;
    }
}
