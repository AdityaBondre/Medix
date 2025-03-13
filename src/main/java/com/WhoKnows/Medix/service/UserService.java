package com.WhoKnows.Medix.service;

import com.WhoKnows.Medix.model.Admin;
import com.WhoKnows.Medix.model.DTOs.Response;
import com.WhoKnows.Medix.model.DTOs.UserProfileUpdateDTO;
import com.WhoKnows.Medix.model.Doctor;
import com.WhoKnows.Medix.model.Patient;
import com.WhoKnows.Medix.model.User;
import com.WhoKnows.Medix.repository.AdminRepository;
import com.WhoKnows.Medix.repository.DoctorRepository;
import com.WhoKnows.Medix.repository.PatientRepository;
import com.WhoKnows.Medix.security.CustomUserDetailsService;
import com.WhoKnows.Medix.security.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private  EmailService emailService;

    // Service method to handle registration
    public ResponseEntity<String> register(User userDto) {
        try {
            if (userDto.getRole().equals(User.ROLE_PATIENT)) {
                Patient patient = new Patient();
                patient.setName(userDto.getName());
                patient.setEmail(userDto.getEmail());
                patient.setGender(userDto.getGender());
                patient.setPassword(userDto.getPassword());
                patient.setRole(User.ROLE_PATIENT);
                patient.setUpdatedAt(null);

                // Encrypt the password before saving
                String encryptedPassword = passwordEncoder.encode(patient.getPassword());
                patient.setPassword(encryptedPassword);

                emailService.sendRegistrationEmail(userDto.getName(), userDto.getEmail());

                patientRepository.save(patient);

            } else if (userDto.getRole().equals(User.ROLE_DOCTOR)) {
                Doctor doctor = new Doctor();
                doctor.setName(userDto.getName());
                doctor.setEmail(userDto.getEmail());
                doctor.setGender(userDto.getGender());
                doctor.setPassword(userDto.getPassword());
                doctor.setRole(User.ROLE_DOCTOR);
                doctor.setStatus(Doctor.STATUS_PENDING);
                doctor.setUpdatedAt(null);

                // Encrypt the password before saving
                String encryptedPassword = passwordEncoder.encode(doctor.getPassword());
                doctor.setPassword(encryptedPassword);

                emailService.sendRegistrationEmail( userDto.getEmail(),userDto.getName());
                doctorRepository.save(doctor);

            } else if (userDto.getRole().equals(User.ROLE_ADMIN)) {
                Admin admin = new Admin();
                admin.setName(userDto.getName());
                admin.setEmail(userDto.getEmail());
                admin.setGender(userDto.getGender());
                admin.setPassword(userDto.getPassword());
                admin.setRole(User.ROLE_ADMIN);
                admin.setUpdatedAt(null);

                // Encrypt the password before saving
                String encryptedPassword = passwordEncoder.encode(admin.getPassword());
                admin.setPassword(encryptedPassword);

                emailService.sendRegistrationEmail(userDto.getName(), userDto.getEmail());

                adminRepository.save(admin);

            } else {
                return ResponseEntity.badRequest().body("Invalid role specified.");
            }

            return ResponseEntity.ok("User registered successfully.");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("Email already exists.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing the registration.");
        }
    }

    // Service method to handle login
    public Map<String, String> login(User user) {
        try {
            // Load user details using the custom user details service
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());

            // Verify the password
            if (passwordEncoder.matches(user.getPassword(), userDetails.getPassword())) {
                // Retrieve the role directly from UserDetails
                String role = userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .findFirst()
                        .orElse("");

                // Generate a token including the email and role
                String token = jwtUtil.generateToken(userDetails.getUsername(), role);

                String updatedAt = "null"; // Default value for first-time login


                // Fetch updatedAt from the correct table based on role
                if ("ROLE_DOCTOR".equals(role)) {
                    Optional<Doctor> doctor = doctorRepository.findByEmail(user.getEmail());
                    updatedAt = doctor.map(d -> d.getUpdatedAt() != null ? d.getUpdatedAt().toString() : "null").orElse("null");
                } else if ("ROLE_PATIENT".equals(role)) {
                    Optional<Patient> patient = patientRepository.findByEmail(user.getEmail());
                    updatedAt = patient.map(p -> p.getUpdatedAt() != null ? p.getUpdatedAt().toString() : "null").orElse("null");
                } else if ("ROLE_ADMIN".equals(role)) {
                    Optional<Admin> admin = adminRepository.findByEmail(user.getEmail());
                    updatedAt = admin.map(a -> a.getUpdatedAt() != null ? a.getUpdatedAt().toString() : "null").orElse("null");
                }

                // Create response map
                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                response.put("role", role);  // Add role in response
                response.put("updatedAt", updatedAt);

                return response;
            } else {
                throw new IllegalArgumentException("Invalid login credentials");
            }
        } catch (UsernameNotFoundException | IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid login credentials");
        }
    }


    // Update Profile

    @Transactional // Ensure DB transaction commits the updates
    public ResponseEntity<Object> updateProfile(Long userId, String role, UserProfileUpdateDTO userDTO) {

        if ("ROLE_PATIENT".equalsIgnoreCase(role)) {
            Patient patient = patientRepository.findById(userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found"));

            // ✅ Update fields if new values are provided (not null)
            if (userDTO.getName() != null) patient.setName(userDTO.getName());
            if (userDTO.getEmail() != null) patient.setEmail(userDTO.getEmail());
            if (userDTO.getPassword() != null) patient.setPassword(passwordEncoder.encode(userDTO.getPassword())); // Encrypt password
            if (userDTO.getGender() != null) patient.setGender(userDTO.getGender());
            if (userDTO.getBloodGroup() != null) patient.setBloodGroup(userDTO.getBloodGroup());
            if (userDTO.getDateOfBirth() != null) patient.setDateOfBirth(userDTO.getDateOfBirth());
            patient.setUpdatedAt(LocalDateTime.now());

            patientRepository.save(patient); // ✅ Force DB update
            return ResponseEntity.ok(new Response("Profile successfully updated!"));
        }

        if ("ROLE_DOCTOR".equalsIgnoreCase(role)) {
            Doctor doctor = doctorRepository.findById(userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor not found"));

            if (userDTO.getName() != null) doctor.setName(userDTO.getName());
            if (userDTO.getEmail() != null) doctor.setEmail(userDTO.getEmail());
            if (userDTO.getPassword() != null) doctor.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            if (userDTO.getGender() != null) doctor.setGender(userDTO.getGender());
            if (userDTO.getSpecialization() != null) doctor.setSpecialization(userDTO.getSpecialization());
            if (userDTO.getLicenseNumber() != null) doctor.setLicenseNumber(userDTO.getLicenseNumber());
            if (userDTO.getHospitalName() != null) doctor.setHospitalName(userDTO.getHospitalName());
            if (userDTO.getDateOfBirth() != null) doctor.setDateOfBirth(userDTO.getDateOfBirth());
            doctor.setUpdatedAt(LocalDateTime.now());

            doctorRepository.save(doctor);
            return ResponseEntity.ok(new Response("Profile updated successfully! Wait for admin approval to proceed."));
        }

        if ("ROLE_ADMIN".equalsIgnoreCase(role)) {
            Admin admin = adminRepository.findById(userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin not found"));

            if (userDTO.getName() != null) admin.setName(userDTO.getName());
            if (userDTO.getEmail() != null) admin.setEmail(userDTO.getEmail());
            if (userDTO.getPassword() != null) admin.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            admin.setUpdatedAt(LocalDateTime.now());

            adminRepository.save(admin);
            return ResponseEntity.ok(new Response("Admin profile updated successfully!"));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response("Access denied for this role"));
    }



    // Fetch user details (based on role)
    public ResponseEntity<Object> getUserDetails(String role, String email) {
        UserProfileUpdateDTO userProfile = new UserProfileUpdateDTO();

        switch (role) {
            case "ROLE_PATIENT":
                Optional<Patient> patient = patientRepository.findByEmail(email);
                if (patient.isPresent()) {
                    Patient p = patient.get();
                    userProfile.setId(p.getId());
                    userProfile.setName(p.getName());
                    userProfile.setEmail(p.getEmail());
                    userProfile.setGender(p.getGender());
                    userProfile.setBloodGroup(p.getBloodGroup());
                    userProfile.setDateOfBirth(p.getDateOfBirth());
                    return ResponseEntity.ok(userProfile);
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Patient not found"));

            case "ROLE_DOCTOR":
                Optional<Doctor> doctor = doctorRepository.findByEmail(email);
                if (doctor.isPresent()) {
                    Doctor d = doctor.get();
                    userProfile.setId(d.getId());
                    userProfile.setName(d.getName());
                    userProfile.setEmail(d.getEmail());
                    userProfile.setGender(d.getGender());
                    userProfile.setSpecialization(d.getSpecialization());
                    userProfile.setLicenseNumber(d.getLicenseNumber());
                    userProfile.setHospitalName(d.getHospitalName());
                    userProfile.setDateOfBirth(d.getDateOfBirth());
                    userProfile.setStatus(d.getStatus());
                    return ResponseEntity.ok(userProfile);
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Doctor not found"));

            case "ROLE_ADMIN":
                Optional<Admin> admin = adminRepository.findByEmail(email);
                if (admin.isPresent()) {
                    Admin a = admin.get();
                    userProfile.setId(a.getId());
                    userProfile.setName(a.getName());
                    userProfile.setEmail(a.getEmail());
                    return ResponseEntity.ok(userProfile);
                }
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Admin not found"));

            default:
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Response("Access denied for this role"));
        }
    }

}