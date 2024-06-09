package com.example.demo.service;

import com.example.demo.dtos.request.clinics.CreateClinicRequest;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.models.Clinic;
import com.example.demo.repositories.ClinicRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClinicServiceImpl implements ClinicService {

    @Autowired
    private ClinicRepository clinicRepository;

    @Override
    @Transactional
    public Clinic create(CreateClinicRequest clinicRequest) {
        Clinic clinic = Clinic.builder()
                .name(clinicRequest.getName())
                .address(clinicRequest.getAddress())
                .phone(clinicRequest.getPhone())
                .introductionHTML(clinicRequest.getIntroductionHTML())
                .introductionMarkdown(clinicRequest.getIntroductionMarkdown())
                .description(clinicRequest.getDescription())
                .image(clinicRequest.getImage())
                .build();
        return clinicRepository.save(clinic);
    }

    @Override
    @Transactional
    public List<Clinic> findAllPopular() {
        return clinicRepository.findAllPopular();
    }

    @Override
    @Transactional
    public Clinic findById(Long id) {
        return clinicRepository.findById(id)
                .orElseThrow(
                        () -> new NotFoundException("Clinic not found!")
                );
    }
}
