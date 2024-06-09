package com.example.demo.service;

import com.example.demo.dtos.request.doctors.CreateDoctorRequest;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.models.*;
import com.example.demo.repositories.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class DoctorUserServiceImpl implements DoctorUserService {
    @Autowired
    private DoctorUserRepository doctorUserRepository;
    @Autowired
    private ClinicRepository clinicRepository;
    @Autowired
    private SpecializationRepository specializationRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    @Transactional
    public DoctorUser create(CreateDoctorRequest request) {
        if(userRepository.existsByEmail(request.getEmail())){
            throw new BadRequestException("email adready exists");
        }
        Role role = roleRepository.findByName("DOCTOR")
                .orElseThrow(
                        () -> new NotFoundException("role doctor is not found")
                );
        User newUser = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .gender(request.getGender())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .address(request.getAddress())
                .role(role)
                .isActive(1)
                .build();
        User user = userRepository.save(newUser);

        Clinic clinic = clinicRepository.findById(request.getClinicId())
                .orElseThrow(
                        () -> new NotFoundException("clinic not found")
                );
        Specialization specialization = specializationRepository.findById(request.getSpecializationId())
                .orElseThrow(
                        () -> new NotFoundException("specialization not found")
                );
        DoctorUser doctorUser = DoctorUser.builder()
                .user(user)
                .clinic(clinic)
                .specialization(specialization)
                .introduce(request.getIntroduce())
                .process(request.getProcess())
                .achievement(request.getAchievement())
                .build();
        return doctorUserRepository.save(doctorUser);
    }

    @Override
    @Transactional
    public void lockUserOfDoctor(Long doctorId) {
        User user = userRepository.findById(doctorId)
                .orElseThrow(
                        () -> new NotFoundException("user not found")
                );
        if (!user.getRole().getName().equals("DOCTOR")) {
            throw new BadRequestException("id is not doctor");
        }
        user.setIsActive(0);
        userRepository.save(user);
    }

    @Override
    public void sendEmailforPatient(String to, String subject, String body, MultipartFile file) throws MessagingException, IOException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body);

        File tempFile = File.createTempFile("file", file.getOriginalFilename());
        file.transferTo(tempFile);
        FileSystemResource resource = new FileSystemResource(tempFile);
//        ByteArrayResource resource = new ByteArrayResource(file.getBytes());
        helper.addAttachment(file.getOriginalFilename(), resource);
        javaMailSender.send(message);
        tempFile.deleteOnExit();

    }


}
