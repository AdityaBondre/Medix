package com.WhoKnows.Medix.repository;

import com.WhoKnows.Medix.model.MedicalHistory;
import com.WhoKnows.Medix.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicalHistoryRepository extends JpaRepository<MedicalHistory,Long> {
    List<MedicalHistory> findByPatient(Patient patient);
}
