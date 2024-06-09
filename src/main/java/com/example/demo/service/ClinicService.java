package com.example.demo.service;

import com.example.demo.dtos.request.clinics.CreateClinicRequest;
import com.example.demo.models.Clinic;
import jakarta.transaction.Transactional;

import java.util.List;

public interface ClinicService {
    @Transactional
    Clinic create(CreateClinicRequest clinicRequest);

    @Transactional
    List<Clinic> findAllPopular();

    @Transactional
    Clinic findById(Long id);
}
