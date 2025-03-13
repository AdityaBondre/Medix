package com.WhoKnows.Medix.controller;

import com.WhoKnows.Medix.model.DTOs.Response;
import com.WhoKnows.Medix.model.DTOs.UserProfileUpdateDTO;
import com.WhoKnows.Medix.model.User;
import com.WhoKnows.Medix.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Map;

@Controller
@RequestMapping("/api/auth")

public class AuthController {

    @Autowired
    private UserService userService;

    // Serve signup page
    @GetMapping("/signup")
    public String showSignupPage() {
        return "signup";  // The name of the Thymeleaf template (without .html)
    }

    // Serve login page
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";  // The name of the Thymeleaf template (without .html)
    }

    @GetMapping("/patient/dashboard")
    public String showDashboardPage() {
        return "patient-dashboard";  // The name of the Thymeleaf template (without .html)
    }

    @GetMapping("/admin/dashboard")
    public String showDashboardAdminPage() {
        return "admin-dashboard";  // The name of the Thymeleaf template (without .html)
    }
    @GetMapping("/doctor/dashboard")
    public String showDashboardDoctorPage() {
        return "doctor-dashboard";  // The name of the Thymeleaf template (without .html)
    }
    @GetMapping("/doctor/profile-update")
    public String showDoctorProfilePage(){
        return "doctor-profile";
    }
    @GetMapping("/patient/profile-update")
    public String showPatientProfilePage(){
        return "patient-profile";
    }




    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody User user) {
        try {
            Map<String, String> loginResponse = userService.login(user);
            return ResponseEntity.ok(loginResponse);  // Return token & role in response
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Collections.singletonMap("message", "Invalid login credentials"));
        }
    }


    // Register endpoint
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        return userService.register(user);

    }


    // Endpoint to update the profile for Patient, Doctor, Admin (based on their role)
    @PutMapping("/profile/{id}")
    public ResponseEntity<Object> updateProfile(@PathVariable Long id, @RequestBody UserProfileUpdateDTO userDTO, Authentication authentication) {
        String email = authentication.getName();  // Use email for authentication
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()  // Assuming one role, adjust if there are multiple roles
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));

        // Call service method to update profile
        return userService.updateProfile(id, role, userDTO);
    }

    // Endpoint to view user details (Patient, Doctor, Admin)
    @GetMapping("/profile/details")
    public ResponseEntity<Object> getUserDetails(Authentication authentication) {
        if (authentication == null || authentication.getAuthorities().isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Response("Unauthorized access"));
        }

        String email = authentication.getName(); // Extract email from authentication
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "User role not found"));

        return userService.getUserDetails(role, email);
    }


}
