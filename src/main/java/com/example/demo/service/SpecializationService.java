package com.example.demo.service;

import com.example.demo.dtos.request.specializations.CreateSpecializationRequest;
import com.example.demo.models.Specialization;

import java.util.List;

public interface SpecializationService {

    Specialization create(CreateSpecializationRequest specializationRequest);

    List<Specialization> findAllPopular();

    Specialization findById(Long id);
}
