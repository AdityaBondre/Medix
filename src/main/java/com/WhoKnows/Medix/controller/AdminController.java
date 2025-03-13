package com.WhoKnows.Medix.controller;

import com.WhoKnows.Medix.model.DTOs.DoctorDTO;
import com.WhoKnows.Medix.model.DTOs.PatientDTO;
import com.WhoKnows.Medix.model.DTOs.Response;
import com.WhoKnows.Medix.model.Doctor;
import com.WhoKnows.Medix.model.Patient;
import com.WhoKnows.Medix.repository.DoctorRepository;
import com.WhoKnows.Medix.repository.PatientRepository;
import com.WhoKnows.Medix.service.DoctorService;
import com.WhoKnows.Medix.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private DoctorService doctorService;
    // Endpoint to view doctors based on their approval status
    @GetMapping("/doctors")
    public ResponseEntity<Object> getDoctorsByStatus(@RequestParam(required = false) String status, Authentication authentication) {


        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Role not found"));

        if (!"ROLE_ADMIN".equalsIgnoreCase(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response("Access denied for this role"));
        }

        // If no status is provided, fetch all doctors
        List<Doctor> doctors;
        if (status == null || status.isEmpty()) {
            doctors = doctorRepository.findAll();
        } else {
            doctors = doctorRepository.findByStatus(status.toUpperCase());
        }

        if (doctors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("No doctors found for the given criteria"));
        }

        // Convert Doctor entities to DoctorDTO
        List<DoctorDTO> doctorDTOs = doctors.stream()
                .map(DoctorDTO::mapToDoctorDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/patients")
    public ResponseEntity<?> getAllPatient(Authentication authentication){
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Role not found"));

        if (!"ROLE_ADMIN".equalsIgnoreCase(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response("Access denied for this role"));
        }
        List <Patient> patients = patientRepository.findAll();
        if (patients.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("No doctors found for the given criteria"));
        }

        List<PatientDTO> patientDTOs = patients.stream()
                .map(PatientDTO::mapToPatientDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(patients);

    }

    // Endpoint to approve a doctor
    @PutMapping("/doctors/{id}/approve")
    public ResponseEntity<Object> approveDoctor(@PathVariable Long id, Authentication authentication) {
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Role not found"));

        // Only admins can approve
        if (!"ROLE_ADMIN".equalsIgnoreCase(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response("Access denied for this role"));
        }

        // Call service to approve doctor
        return doctorService.changeDoctorStatus(id, Doctor.STATUS_APPROVED);
    }

    // Endpoint to reject a doctor
    @PutMapping("/doctors/{id}/reject")
    public ResponseEntity<Object> rejectDoctor(@PathVariable Long id, Authentication authentication) {
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Role not found"));

        // Only admins can reject
        if (!"ROLE_ADMIN".equalsIgnoreCase(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response("Access denied for this role"));
        }

        // Call service to reject doctor
        return doctorService.changeDoctorStatus(id, Doctor.STATUS_REJECTED);
    }

    @PostMapping("/doctors/{doctorId}/send-profile-update-email")
    public ResponseEntity<String> sendProfileUpdateEmail(@PathVariable Long doctorId) {
        Optional<Doctor> doctorOptional = doctorRepository.findById(doctorId);

        if (doctorOptional.isPresent()) {
            Doctor doctor = doctorOptional.get();

            // Check if profile is not updated
            if (doctor.getUpdatedAt() == null) {
                emailService.sendProfileUpdateEmail(doctor.getEmail(), doctor.getName());
                return ResponseEntity.ok("Profile update email sent successfully!");
            } else {
                return ResponseEntity.badRequest().body("Doctor has already updated their profile.");
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor not found.");
    }


}
