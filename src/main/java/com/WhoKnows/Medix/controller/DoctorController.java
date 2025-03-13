package com.WhoKnows.Medix.controller;

import com.WhoKnows.Medix.model.Appointment;
import com.WhoKnows.Medix.model.DTOs.AppointmentApproveDTO;
import com.WhoKnows.Medix.model.DTOs.AppointmentDTO;
import com.WhoKnows.Medix.model.DTOs.MedicalHistoryDTO;
import com.WhoKnows.Medix.model.DTOs.RescheduleAppointmentDTO;
import com.WhoKnows.Medix.model.Patient;
import com.WhoKnows.Medix.repository.AppointmentRepository;
import com.WhoKnows.Medix.repository.DoctorRepository;
import com.WhoKnows.Medix.repository.PatientRepository;
import com.WhoKnows.Medix.service.AppointmentService;
import com.WhoKnows.Medix.service.MedicalHistoryService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private MedicalHistoryService medicalHistoryService;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Value("${python.api.url}")  // Inject URL from properties file
    private String pythonApiUrl;



    // end point for record audio and convert it into text by using python model and get summary of it
    @PostMapping("/convert-audio/{appointmentId}")
    public ResponseEntity<Map<String, String>> convertAudioToText(@RequestParam("file") MultipartFile file,@PathVariable Long appointmentId, Authentication authentication) {
        try {


            //  1. Save uploaded file to your specified directory
            // Example path : C:\Who Knows\Markme - Copy\src\main\resources\recordings
            File recordingsDir = new File("src\\main\\resources\\recordings");
            if (!recordingsDir.exists()) {
                recordingsDir.mkdirs(); // Create dir if it doesn't exist
            }

            File audioFile = new File(recordingsDir, file.getOriginalFilename());
            file.transferTo(audioFile); // Save file

            //  2. Prepare request to Python Whisper API
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new FileSystemResource(audioFile)); // Send saved file

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);


            //  3. Make the API call
            ResponseEntity<String> response = restTemplate.postForEntity(pythonApiUrl, requestEntity, String.class);

            //  4. Parse the response to extract only the summary
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            String summary = root.path("summary").asText("No summary found");

            medicalHistoryService.addMedicalHistory(appointmentId,summary);

            //  5. Return only the summary
            return ResponseEntity.ok(Collections.singletonMap("summary", summary));

        } catch (IOException | RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Error: " + e.getMessage()));
        }
    }





    // Endpoint for check authentication of videocall
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


    @GetMapping("/status")
    public ResponseEntity<Map<String, String>> getDoctorStatus(Authentication authentication) {
        String doctorEmail = authentication.getName();

        // Fetch the doctor's status using the repository method
        String status = doctorRepository.findStatusByEmail(doctorEmail);


        if (status != null) {
            // Return JSON response with status
            return ResponseEntity.ok(Map.of("status", status));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Doctor not found"));
        }
    }



    // Endpoint for doctors to approve an appointment ->done
    @PutMapping("/{id}/approve")
    public ResponseEntity<String> approveAppointment(
            @PathVariable Long id,
            @RequestBody AppointmentApproveDTO appointmentApproveDTO, // Receive time as a string (e.g., "14:30")
            Authentication authentication) {

        String doctorEmail = authentication.getName(); // Get the authenticated doctor's email
        String time = appointmentApproveDTO.getTime();

        // Convert the string time to LocalTime
        LocalTime appointmentTime;
        try {
            appointmentTime = LocalTime.parse(time); // Convert the time string to LocalTime
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid time format. Please use HH:mm.");
        }

        // Call the service method with parameters in the correct order
        return appointmentService.approveAppointment(id, doctorEmail, appointmentTime);
    }


    // Endpoint for doctors to reject an appointment -> done
    @PutMapping("/{id}/reject")
    public ResponseEntity<String> rejectAppointment(@PathVariable Long id, Authentication authentication) {
        String doctorEmail = authentication.getName(); // Authenticated doctor's email
        appointmentService.cancelAppointmentByDoctor(id, doctorEmail);
        return ResponseEntity.ok("Appointment rejected successfully.");
    }

    // Endpoint for doctors to cancel an appointment
    @DeleteMapping("/{id}/cancel-by-doctor")
    public ResponseEntity<String> cancelAppointmentByDoctor(@PathVariable Long id, Authentication authentication) {
        String doctorEmail = authentication.getName(); // Authenticated doctor's email
        appointmentService.cancelAppointmentByDoctor(id, doctorEmail);
        return ResponseEntity.ok("Appointment canceled successfully by the doctor.");
    }

    // Endpoint for doctors to reschedule an appointment -> done
    @PutMapping("/{id}/reschedule")
    public ResponseEntity<String> rescheduleAppointment(
            @PathVariable Long id,
            @RequestBody RescheduleAppointmentDTO rescheduleDTO,
            Authentication authentication) {
        String doctorEmail = authentication.getName(); // Authenticated doctor's email
        String response = String.valueOf(appointmentService.rescheduleAppointment(id, rescheduleDTO, doctorEmail));
        return ResponseEntity.ok(response);
    }

    // Endpoint for doctors to view their appointments -> done
    @GetMapping("/doctor")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsForDoctor(
            Authentication authentication,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate appointmentDate) {
        try {
            String doctorEmail = authentication.getName(); // Authenticated doctor's email

            // If no date is provided, default to today
            if (appointmentDate == null) {
                appointmentDate = LocalDate.now();
            }

            // Call the service method that returns List<AppointmentDTO>
            List<AppointmentDTO> appointments = appointmentService.getAppointmentsForDoctorList(doctorEmail, status, appointmentDate);

            // Return ResponseEntity with the list of appointments
            return ResponseEntity.ok(appointments);

        } catch (IllegalArgumentException e) {
            // If an exception is thrown, return a bad request with an empty list
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
    }

    // End Point for view patients medical history
    @GetMapping("/medical-history/{patientId}")
    public ResponseEntity<?> getMedicalHistoryForPatient(@PathVariable Long patientId, Authentication authentication) {
        try {
            Patient patient = patientRepository.findById(patientId)
                    .orElseThrow(() -> new IllegalArgumentException("Patient not found"));

            String patientEmail = patient.getEmail();
            List<MedicalHistoryDTO> historyDTOList = medicalHistoryService.getMedicalHistoryForPatient(patientEmail);

            return ResponseEntity.ok(historyDTOList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error fetching medical history: " + e.getMessage());
        }
    }


    // Update Medical history
    @PutMapping("/{historyId}/update-summary")
    public ResponseEntity<Map<String, String>> updateSummary(@PathVariable Long historyId, @RequestBody Map<String, String> request) {
        String newSummary = request.get("summary");
        String message = medicalHistoryService.updateMedicalHistory(historyId, newSummary);

        Map<String, String> response = new HashMap<>();
        response.put("message", message);

        return ResponseEntity.ok(response);
    }




}
