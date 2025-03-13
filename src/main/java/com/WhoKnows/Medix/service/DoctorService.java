package com.WhoKnows.Medix.service;

import com.WhoKnows.Medix.model.DTOs.DoctorDTO;
import com.WhoKnows.Medix.model.DTOs.Response;
import com.WhoKnows.Medix.model.Doctor;
import com.WhoKnows.Medix.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private EmailService emailService;

    public ResponseEntity<Object> changeDoctorStatus(Long doctorId, String status) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // Update the status
        doctor.setStatus(status);
        doctorRepository.save(doctor);

        // Send email notification
        if (status.equals(Doctor.STATUS_APPROVED)) {
            emailService.sendDoctorApprovalEmail(doctor.getEmail(), doctor.getName());
        } else {
            emailService.sendDoctorRejectionEmail(doctor.getEmail(), doctor.getName());
        }

        String message = status.equals(Doctor.STATUS_APPROVED)
                ? "Doctor has been approved successfully."
                : "Doctor has been rejected successfully.";

        return ResponseEntity.ok(new Response(message));
    }


    public List<DoctorDTO> searchDoctors(String gender, String specialization, String hospitalName, String name) {
        List<Doctor> doctors = doctorRepository.findDoctors(gender, specialization, hospitalName, name);

        return doctors.stream()
                .map(DoctorDTO::mapToDoctorDTO)
                .toList();
    }


}