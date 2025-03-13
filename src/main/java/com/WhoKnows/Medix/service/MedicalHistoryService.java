package com.WhoKnows.Medix.service;

import com.WhoKnows.Medix.model.Appointment;
import com.WhoKnows.Medix.model.DTOs.MedicalHistoryDTO;
import com.WhoKnows.Medix.model.Doctor;
import com.WhoKnows.Medix.model.MedicalHistory;
import com.WhoKnows.Medix.model.Patient;
import com.WhoKnows.Medix.repository.AppointmentRepository;
import com.WhoKnows.Medix.repository.DoctorRepository;
import com.WhoKnows.Medix.repository.MedicalHistoryRepository;
import com.WhoKnows.Medix.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class MedicalHistoryService {

    @Autowired
    private MedicalHistoryRepository medicalHistoryRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;

    // 8. Add  Appointment Summary by Doctors
    @Transactional
    public ResponseEntity<String> addMedicalHistory(Long appointmentId, String summary) {
        try {
            // Fetch the appointment
            Appointment appointment = appointmentRepository.findById(appointmentId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment not found"));

            Doctor doctor = doctorRepository.findById(appointment.getDoctor().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));

            Patient patient = patientRepository.findByEmail(appointment.getPatient().getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
            MedicalHistory medicalHistory = new MedicalHistory();
            medicalHistory.setSummary(summary);
            medicalHistory.setAppointment(appointment);
            medicalHistory.setDoctor(doctor);
            medicalHistory.setPatient(patient);
            medicalHistoryRepository.save(medicalHistory);
            return ResponseEntity.ok("Appointment Summary Added Successfully");
        } catch (IllegalArgumentException e) {
            // Return error response
            return ResponseEntity.badRequest().body(e.getMessage());

        }

    }

    // view  patient medical history
    public List<MedicalHistoryDTO> getMedicalHistoryForPatient(String patientEmail) {
        Patient patient = patientRepository.findByEmail(patientEmail)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        List<MedicalHistory> medicalHistoryList = medicalHistoryRepository.findByPatient(patient);

        // Convert MedicalHistory list to MedicalHistoryDTO list
        return medicalHistoryList.stream()
                .map(MedicalHistoryDTO::mapToMedicalHistoryDTO)
                .toList();
    }


    //Update Summary By doctors
    @Transactional
    public String updateMedicalHistory(Long historyId, String newSummary) {
        MedicalHistory medicalHistory = medicalHistoryRepository.findById(historyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Medical history not found"));

        medicalHistory.setSummary(newSummary);
        medicalHistoryRepository.save(medicalHistory);

        return "Medical history updated successfully";
    }


}
