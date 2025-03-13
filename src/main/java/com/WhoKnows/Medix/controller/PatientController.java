package com.WhoKnows.Medix.controller;

import com.WhoKnows.Medix.model.Appointment;
import com.WhoKnows.Medix.model.DTOs.AppointmentDTO;
import com.WhoKnows.Medix.model.DTOs.AppointmentRequestDTO;
import com.WhoKnows.Medix.model.DTOs.DoctorDTO;
import com.WhoKnows.Medix.model.DTOs.MedicalHistoryDTO;
import com.WhoKnows.Medix.model.Doctor;
import com.WhoKnows.Medix.model.MedicalHistory;
import com.WhoKnows.Medix.repository.AppointmentRepository;
import com.WhoKnows.Medix.service.AppointmentService;
import com.WhoKnows.Medix.service.DoctorService;
import com.WhoKnows.Medix.service.MedicalHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private MedicalHistoryService medicalHistoryService;


    @GetMapping("/appointments/{appointmentId}/video-call")
    public ResponseEntity<?> getVideoCallUrl(@PathVariable Long appointmentId, Authentication authentication) {
        // Extract user email from JWT token (assuming you have a method to do this)
        String userEmail = authentication.getName();

        // Fetch the appointment
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment not found"));

        // Check if the logged-in user is either the doctor or the patient of this appointment
        if (!appointment.getDoctor().getEmail().equals(userEmail) && !appointment.getPatient().getEmail().equals(userEmail)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to access this video call.");
        }

        // Ensure the appointment is virtual and approved
        if (!Appointment.TYPE_VIRTUAL.equals(appointment.getAppointmentType()) || !Appointment.STATUS_APPROVED.equals(appointment.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This appointment is not eligible for a video call.");
        }

        // Return the video call URL
        return ResponseEntity.ok(Map.of("videoCallUrl", appointment.getVideoCallUrl()));
    }

    @GetMapping("/search")
    public ResponseEntity<List<DoctorDTO>> searchDoctors(
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) String hospitalName,
            @RequestParam(required = false) String name) {

        List<DoctorDTO> doctors = doctorService.searchDoctors(gender, specialization, hospitalName, name);
        return ResponseEntity.ok(doctors);
    }


    // Endpoint for patients to request an appointment ->done
    @PostMapping("/request")
    public ResponseEntity<String> requestAppointment(@RequestBody AppointmentRequestDTO requestDTO, Authentication authentication) {
        String patientEmail = authentication.getName(); // Authenticated patient's email
       String response = String.valueOf(appointmentService.bookAppointment(requestDTO, patientEmail));
        return ResponseEntity.ok(response);
    }

    // Endpoint for patients to cancel their own appointment
    @DeleteMapping("/{id}/cancel-by-patient")
    public ResponseEntity<String> cancelAppointmentByPatient(@PathVariable Long id, Authentication authentication) {
        String patientEmail = authentication.getName(); // Authenticated patient's email
        appointmentService.cancelAppointmentByPatient(id, patientEmail);
        return ResponseEntity.ok("Appointment canceled successfully by the patient.");
    }

    // Endpoint for patients to view their appointments ->done
    @GetMapping("/patient")
    public ResponseEntity<?> getAppointmentsForPatient(Authentication authentication) {
        try {
            String patientEmail = authentication.getName();
            List<AppointmentDTO> appointments = appointmentService.getAppointmentsForPatient(patientEmail);
            Collections.reverse(appointments); // Reverse the list if needed
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error fetching appointments: " + e.getMessage());
        }
    }


    @GetMapping("/medical-history")
    public ResponseEntity<?> getMedicalHistoryForPatient(Authentication authentication) {
        try {
            String patientEmail = authentication.getName();
            List<MedicalHistoryDTO> historyDTOList = medicalHistoryService.getMedicalHistoryForPatient(patientEmail);

            return ResponseEntity.ok(historyDTOList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error fetching medical history: " + e.getMessage());
        }
    }

}
