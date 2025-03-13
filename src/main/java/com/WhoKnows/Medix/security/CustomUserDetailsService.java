package com.WhoKnows.Medix.security;

import com.WhoKnows.Medix.model.Admin;
import com.WhoKnows.Medix.model.Doctor;
import com.WhoKnows.Medix.model.Patient;
import com.WhoKnows.Medix.repository.AdminRepository;
import com.WhoKnows.Medix.repository.DoctorRepository;
import com.WhoKnows.Medix.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;


@Service
public class CustomUserDetailsService implements UserDetailsService {



    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Attempt to find the user in the Patient repository
        Optional<Patient> patientOpt = patientRepository.findByEmail(username);
        if (patientOpt.isPresent()) {
            Patient patient = patientOpt.get();
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_PATIENT")); // Add the appropriate role
            return new org.springframework.security.core.userdetails.User(patient.getEmail(), patient.getPassword(), authorities);
        }

        // Attempt to find the user in the Doctor repository
        Optional<Doctor> doctorOpt = doctorRepository.findByEmail(username);
        if (doctorOpt.isPresent()) {
            Doctor doctor = doctorOpt.get();
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_DOCTOR")); // Add the appropriate role
            return new org.springframework.security.core.userdetails.User(doctor.getEmail(), doctor.getPassword(), authorities);
        }

        // Attempt to find the user in the Admin repository
        Optional<Admin> adminOpt = adminRepository.findByEmail(username);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN")); // Add the appropriate role
            return new org.springframework.security.core.userdetails.User(admin.getEmail(), admin.getPassword(), authorities);
        }

        // If no user is found in any repository, throw an exception
        throw new UsernameNotFoundException("User not found with email: " + username);
    }


}
