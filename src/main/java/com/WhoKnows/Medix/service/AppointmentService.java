package com.WhoKnows.Medix.service;

import com.WhoKnows.Medix.model.Appointment;
import com.WhoKnows.Medix.model.DTOs.AppointmentDTO;
import com.WhoKnows.Medix.model.DTOs.AppointmentRequestDTO;
import com.WhoKnows.Medix.model.DTOs.RescheduleAppointmentDTO;
import com.WhoKnows.Medix.model.Doctor;
import com.WhoKnows.Medix.model.Patient;
import com.WhoKnows.Medix.repository.AppointmentRepository;
import com.WhoKnows.Medix.repository.DoctorRepository;
import com.WhoKnows.Medix.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.stringtemplate.v4.ST;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private  DoctorRepository doctorRepository;
    @Autowired
    private  PatientRepository patientRepository;
    @Autowired
    private DailyCoService dailyCoService;
    @Autowired
    private EmailService emailService;

    // 1. Patient requests an appointment
    @Transactional
    public ResponseEntity<String> bookAppointment(AppointmentRequestDTO requestDTO, String patientEmail) {
        try {
            Doctor doctor = doctorRepository.findById(requestDTO.getDoctorId())
                    .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));

            Patient patient = patientRepository.findByEmail(patientEmail)
                    .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

            if (requestDTO.getAppointmentDate().isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Appointment date cannot be in the past.");
            }

            // Save appointment as pending
            Appointment appointment = new Appointment();
            appointment.setDoctor(doctor);
            appointment.setPatient(patient);
            appointment.setAppointmentDate(requestDTO.getAppointmentDate());
            appointment.setAppointmentType(requestDTO.getAppointmentType());
            appointment.setStatus(Appointment.STATUS_PENDING);
            appointmentRepository.save(appointment);
            emailService.sendAppointmentBookingEmail(patientEmail,
                    patient.getName(),doctor.getName(),
                    requestDTO.getAppointmentDate(),
                    requestDTO.getAppointmentType());

            return ResponseEntity.ok("Appointment requested successfully. Waiting for doctor's approval.");
        } catch (IllegalArgumentException e) {
            emailService.sendAppointmentFailureEmail(patientEmail,requestDTO.getAppointmentDate());
            // Return exception message in the response
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    // 2. Doctor approves the appointment and sets the time
    @Transactional
    public ResponseEntity<String> approveAppointment(Long appointmentId, String doctorEmail, LocalTime appointmentTime) {
        try {
            // Find the appointment
            Appointment appointment = appointmentRepository.findById(appointmentId)
                    .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

            // Validate doctor
            if (!appointment.getDoctor().getEmail().equals(doctorEmail)) {
                throw new IllegalArgumentException("You are not authorized to approve this appointment.");
            }

            // Ensure the appointment is pending
            if (!appointment.getStatus().equals(Appointment.STATUS_PENDING)) {
                throw new IllegalArgumentException("Only pending appointments can be approved.");
            }

            // Check for time conflict
            boolean isTimeConflict = appointmentRepository.existsByDoctorAndAppointmentDateAndAppointmentTimeAndStatus(
                    appointment.getDoctor(), appointment.getAppointmentDate(), appointmentTime, Appointment.STATUS_APPROVED);

            if (isTimeConflict) {
                throw new IllegalArgumentException("This appointment time is already booked by another patient.");
            }

            // Set appointment time and status
            appointment.setAppointmentTime(appointmentTime);
            appointment.setStatus(Appointment.STATUS_APPROVED);

            // Generate video call link (Daily.co)
            if (Appointment.TYPE_VIRTUAL.equals(appointment.getAppointmentType())) {
                String videoCallUrl = dailyCoService.createMeeting(
                        appointment.getDoctor().getId().toString(),
                        appointment.getPatient().getId().toString()
                );
                appointment.setVideoCallUrl(videoCallUrl);
            } else {
                appointment.setVideoCallUrl(null);
            }

            emailService.sendAppointmentApprovalEmail(appointment.getPatient().getEmail(),
                    appointment.getPatient().getName(),appointment.getDoctor().getName(),
                    appointment.getAppointmentDate(),appointmentTime);
            // Save the updated appointment
            appointmentRepository.save(appointment);

            return ResponseEntity.ok("Appointment approved successfully.");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 3. Patient cancels an appointment
    @Transactional
    public ResponseEntity<String> cancelAppointmentByPatient(Long appointmentId, String patientEmail) {
        try {
            Appointment appointment = appointmentRepository.findById(appointmentId)
                    .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

            if (!appointment.getPatient().getEmail().equals(patientEmail)) {
                throw new IllegalArgumentException("You are not authorized to cancel this appointment.");
            }

            if (appointment.getStatus().equals(Appointment.STATUS_CANCELLED)) {
                throw new IllegalArgumentException("Appointment is already cancelled.");
            }

            appointment.setStatus(Appointment.STATUS_CANCELLED);
            emailService.sendAppointmentCancellationEmailToPatient(appointment.getPatient().getEmail(),
                    appointment.getDoctor().getName(),appointment.getPatient().getName(),appointment.getAppointmentDate(),appointment.getAppointmentTime());
            appointmentRepository.save(appointment);

            return ResponseEntity.ok("Appointment cancelled successfully.");

        } catch (IllegalArgumentException e) {
            // Return exception message in the response
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 4. Doctor cancels an appointment
    @Transactional
    public ResponseEntity<String> cancelAppointmentByDoctor(Long appointmentId, String doctorEmail) {
        try {
            Appointment appointment = appointmentRepository.findById(appointmentId)
                    .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

            if (!appointment.getDoctor().getEmail().equals(doctorEmail)) {
                throw new IllegalArgumentException("You are not authorized to cancel this appointment.");
            }

            if (appointment.getStatus().equals(Appointment.STATUS_CANCELLED)) {
                throw new IllegalArgumentException("Appointment is already cancelled.");
            }

            appointment.setStatus(Appointment.STATUS_CANCELLED);
            emailService.sendAppointmentCancellationEmailByDoctor(appointment.getPatient().getEmail(),
                    appointment.getPatient().getName(),appointment.getDoctor().getName(),
                    appointment.getAppointmentDate(),appointment.getAppointmentTime());

            appointmentRepository.save(appointment);

            return ResponseEntity.ok("Appointment cancelled successfully.");

        } catch (IllegalArgumentException e) {
            // Return exception message in the response
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 5. reject Appointment By doctor
    @Transactional
    public ResponseEntity<String> rejectAppointmentByDoctor(Long appointmentId, String doctorEmail) {
        try {
            Appointment appointment = appointmentRepository.findById(appointmentId)
                    .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

            if (!appointment.getDoctor().getEmail().equals(doctorEmail)) {
                throw new IllegalArgumentException("You are not authorized to cancel this appointment.");
            }

            if (appointment.getStatus().equals(Appointment.STATUS_CANCELLED)) {
                throw new IllegalArgumentException("Appointment is already cancelled.");
            }

            appointment.setStatus(Appointment.STATUS_CANCELLED);
            emailService.sendAppointmentRejectionEmail(appointment.getPatient().getEmail(),
                    appointment.getPatient().getName(),appointment.getDoctor().getName(),
                    appointment.getAppointmentDate());

            appointmentRepository.save(appointment);

            return ResponseEntity.ok("Appointment cancelled successfully.");

        } catch (IllegalArgumentException e) {
            // Return exception message in the response
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    // 6. Doctor reschedules an appointment
    @Transactional
    public ResponseEntity<String> rescheduleAppointment(Long appointmentId, RescheduleAppointmentDTO rescheduleDTO, String doctorEmail) {
        try {
            // Parse the new date and time
            LocalDate newDate = rescheduleDTO.getNewDate();

            LocalTime newTime = rescheduleDTO.getNewTime();
            // Fetch appointment
            Appointment appointment = appointmentRepository.findById(appointmentId)
                    .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

            // Check authorization
            if (!appointment.getDoctor().getEmail().equals(doctorEmail)) {
                throw new IllegalArgumentException("You are not authorized to reschedule this appointment.");
            }
            // Ensure only approved appointments can be rescheduled
            if (appointment.getStatus().equals(Appointment.STATUS_RESCHEDULED)) {
                throw new IllegalArgumentException("Only one time can rescheduled an appointment.");
            } else if (!appointment.getStatus().equals(Appointment.STATUS_APPROVED)) {
                throw new IllegalArgumentException("Only approved appointments can be rescheduled.");
            }

            // Retain existing date and time if null is provided
            if (newDate == null) {
                newDate = appointment.getAppointmentDate();
            }
            if (newTime == null) {
                newTime = appointment.getAppointmentTime();
            }

            // Check for conflicts
            boolean isTimeConflict = appointmentRepository.existsByDoctorAndAppointmentDateAndAppointmentTimeAndStatus(
                    appointment.getDoctor(), newDate, newTime, Appointment.STATUS_APPROVED);
            if (isTimeConflict) {
                throw new IllegalArgumentException("The new appointment time conflicts with another appointment.");
            }

            // Update appointment details
            appointment.setAppointmentDate(newDate);
            appointment.setAppointmentTime(newTime);
            appointment.setStatus(Appointment.STATUS_RESCHEDULED);

            emailService.sendAppointmentRescheduledEmail(appointment.getPatient().getEmail(),
                    appointment.getPatient().getName(),appointment.getDoctor().getName(),
                    appointment.getAppointmentDate(),rescheduleDTO.getNewDate(),appointment.getAppointmentTime(),rescheduleDTO.getNewTime());

            // Save to the database
            appointmentRepository.save(appointment);

            return ResponseEntity.ok("Appointment rescheduled successfully to: " + newDate + " at " + newTime);
        } catch (IllegalArgumentException e) {
            // Return error response
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    // 7. View all appointments for a doctor
    public List<AppointmentDTO> getAppointmentsForDoctorList(String doctorEmail, String status, LocalDate appointmentDate) {
        Doctor doctor = doctorRepository.findByEmail(doctorEmail)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));

        List<Appointment> appointments;
        if (status != null) {
            appointments = appointmentRepository.findByDoctorAndStatusAndAppointmentDate(doctor, status, appointmentDate);
        } else {
            appointments = appointmentRepository.findByDoctorAndAppointmentDate(doctor, appointmentDate);
        }

        // Map each Appointment to AppointmentDTO using the static method
        return appointments.stream()
                .map(AppointmentDTO::mapToAppointmentDTO) // Method reference
                .collect(Collectors.toList());
    }


    // 8. View all appointments for a patient
    public List<AppointmentDTO> getAppointmentsForPatient(String patientEmail) {
        Patient patient = patientRepository.findByEmail(patientEmail)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        List<Appointment> appointments = appointmentRepository.findByPatient(patient);

        // Map each Appointment to AppointmentDTO
        return appointments.stream()
                .map(AppointmentDTO::mapToAppointmentDTO) // Method reference
                .collect(Collectors.toList());
    }




}