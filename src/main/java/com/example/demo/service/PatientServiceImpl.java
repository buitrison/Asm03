package com.example.demo.service;

import com.example.demo.dtos.request.patients.CreatePatientRequest;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.models.*;
import com.example.demo.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private DoctorUserRepository doctorUserRepository;


    @Override
    @Transactional
    public Patient create(CreatePatientRequest patientRequest, User user) {
        User doctor = userRepository.findByName(patientRequest.getDoctorName())
                .orElseThrow(
                        () -> new NotFoundException("doctor not found")
                );
        if (doctor.getRole().getId() != 2l) {
            throw new BadRequestException("account isnt doctor");
        }

        DoctorUser doctorUser = doctorUserRepository.findById(doctor.getId())
                .orElseThrow(
                        () -> new NotFoundException("user not doctor")
                );

//        boolean isScheduleAvailable = scheduleRepository
//                .isScheduleAvailable(doctor.getId(), patientRequest.getDate(), patientRequest.getTime());
//        if (!isScheduleAvailable) {
//            throw new NotFoundException("schedule not available");
//        }

        Status status = statusRepository.findById(3l)
                .orElseThrow(
                        () -> new NotFoundException("status PENDING is not found in database by Status Id 3")
                );

        Patient patient = Patient.builder()
                .doctor(doctor)
                .clinic(doctorUser.getClinic())
                .specialization(doctorUser.getSpecialization())
                .status(status)
                .name(user.getName())
                .price(patientRequest.getPrice())
                .dateBooking(patientRequest.getDate())
                .timeBooking(patientRequest.getTime())
                .gender(user.getGender())
                .email(user.getEmail())
                .phone(user.getPhone())
                .year(patientRequest.getDateOfBirth())
                .address(user.getAddress())
                .reason(patientRequest.getReason())
                .build();
        return patientRepository.save(patient);
    }


    @Override
    @Transactional
    public List<Patient> findPatientsByDoctorId(Long id) {
        return patientRepository.findPatientsByDoctorId(id);
    }

    @Override
    public List<Patient> findPatientsByDoctorIdAndStatus(Long id, String status) {
        return patientRepository.findPatientsByDoctorIdWithStatus(id, status);
    }

    @Override
    @Transactional
    public Patient updatePatientStatusFromPending(Patient patient, String newStatus, String description) {

        if (!patient.getStatus().getName().equals("PENDING")) {
            throw new BadRequestException("patient status is not pending");
        }
        Status status = statusRepository.findByName(newStatus.toUpperCase())
                .orElseThrow(
                        () -> new NotFoundException("status " + newStatus.toUpperCase() + " is not found")
                );
        patient.setStatus(status);
        if (newStatus.toUpperCase().equals("CANCEL")) {
            patient.setDescription(description);
        }
        return patientRepository.save(patient);
    }

    @Override
    @Transactional
    public List<Patient> findByEmail(String email) {
        return patientRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public Optional<Patient> findById(Long id) {
        return patientRepository.findById(id);
    }

}
