package com.example.demo.service;

import com.example.demo.dtos.request.specializations.CreateSpecializationRequest;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.models.Specialization;
import com.example.demo.repositories.SpecializationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecializationServiceImpl implements SpecializationService {

    @Autowired
    private SpecializationRepository specializationRepository;

    @Override
    @Transactional
    public Specialization create(CreateSpecializationRequest specializationRequest) {
        Specialization specialization = Specialization.builder()
                .name(specializationRequest.getName())
                .image(specializationRequest.getImage())
                .description(specializationRequest.getDescription())
                .build();
        return specializationRepository.save(specialization);
    }

    @Override
    @Transactional
    public List<Specialization> findAllPopular() {
        return specializationRepository.findAllPopular();
    }

    @Override
    @Transactional
    public Specialization findById(Long id) {
        return specializationRepository.findById(id)
                .orElseThrow(
                        () -> new NotFoundException("Specialization not found!")
                );
    }
}
