package com.WhoKnows.Medix.repository;

import com.WhoKnows.Medix.model.Appointment;
import com.WhoKnows.Medix.model.Doctor;
import com.WhoKnows.Medix.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment,Long> {
    List<Appointment> findByPatient(Patient patient);

    List<Appointment> findByDoctor(Doctor doctor);

    List<Appointment> findByDoctorAndStatus(Doctor doctor, String status);

    boolean existsByDoctorAndAppointmentDateAndAppointmentTimeAndStatus(
            Doctor doctor, LocalDate appointmentDate, LocalTime appointmentTime, String status);


    List<Appointment> findByDoctorAndStatusAndAppointmentDate(Doctor doctor, String status, LocalDate appointmentDate);

    List<Appointment> findByDoctorAndAppointmentDate(Doctor doctor, LocalDate appointmentDate);
}
