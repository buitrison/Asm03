package com.example.demo.repositories;

import com.example.demo.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query("SELECT p FROM Patient p WHERE p.doctor.id =:doctorId")
    List<Patient> findPatientsByDoctorId(@Param("doctorId") Long doctorId);

    @Query("SELECT p FROM Patient p WHERE p.doctor.id =:doctorId AND p.status.name =:status")
    List<Patient> findPatientsByDoctorIdWithStatus(@Param("doctorId") Long doctorId, @Param("status") String status);

//    @Query("SELECT p FROM Patient p WHERE p.email =:email")
    List<Patient> findByEmail(String email);
}