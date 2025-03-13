package com.WhoKnows.Medix.repository;

import com.WhoKnows.Medix.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    List<Doctor> findByStatus(String status);
    Optional<Doctor> findByEmail(String email);

    @Query("SELECT d FROM Doctor d WHERE d.status = 'APPROVED' " +
            "AND (:gender IS NULL OR d.gender = :gender) " +
            "AND (:specialization IS NULL OR d.specialization LIKE %:specialization%) " +
            "AND (:hospitalName IS NULL OR d.hospitalName LIKE %:hospitalName%) " +
            "AND (:name IS NULL OR d.name LIKE %:name%)")
    List<Doctor> findDoctors(
            @Param("gender") String gender,
            @Param("specialization") String specialization,
            @Param("hospitalName") String hospitalName,
            @Param("name") String name);

    @Query("SELECT d.status FROM Doctor d WHERE d.email = :email")
    String findStatusByEmail(@Param("email") String email);

}