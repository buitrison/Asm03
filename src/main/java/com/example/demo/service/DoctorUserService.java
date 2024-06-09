package com.example.demo.service;

import com.example.demo.dtos.request.doctors.CreateDoctorRequest;
import com.example.demo.models.DoctorUser;
import jakarta.mail.MessagingException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface DoctorUserService {
    DoctorUser create(CreateDoctorRequest doctorRequest);

    void lockUserOfDoctor(Long doctorId);

    void sendEmailforPatient(String to, String subject, String body, MultipartFile file) throws MessagingException, IOException;
}
