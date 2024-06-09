package com.example.demo.service;

import com.example.demo.dtos.request.patients.CreatePatientRequest;
import com.example.demo.models.Patient;
import com.example.demo.models.User;

import java.util.List;
import java.util.Optional;

public interface PatientService {

    Patient create(CreatePatientRequest patientRequest, User user);

    List<Patient> findPatientsByDoctorId(Long id);

    List<Patient> findPatientsByDoctorIdAndStatus(Long id, String status);

    Patient updatePatientStatusFromPending(Patient patient, String newStatus, String description);

    List<Patient> findByEmail(String email);

    Optional<Patient> findById(Long id);
}
