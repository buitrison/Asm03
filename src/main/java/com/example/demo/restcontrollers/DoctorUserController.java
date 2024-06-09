package com.example.demo.restcontrollers;

import com.example.demo.dtos.request.doctors.CreateDoctorRequest;
import com.example.demo.dtos.response.DataResponse;
import com.example.demo.dtos.response.ErrorResponse;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.exceptions.UnauthorizedException;
import com.example.demo.jwt.JwtTokenUtils;
import com.example.demo.models.DoctorUser;
import com.example.demo.models.Patient;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.service.DoctorUserService;
import com.example.demo.service.PatientService;
import com.example.demo.service.UserService;
import com.example.demo.utils.CustomMap;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("${api.prefix}/doctors")
public class DoctorUserController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private UserService userService;

    @Autowired
    private DoctorUserService doctorUserService;

    // bác sĩ lấy danh sách bệnh nhân đã khám và bệnh lý
    @GetMapping("/{doctorId}/patients")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> findPatientsByDoctor(@PathVariable("doctorId") Long doctorId) {
        User doctor = userService.findUserById(doctorId).orElseThrow(() -> new NotFoundException("doctor not found"));
        if (!doctor.getRole().getName().equals("DOCTOR")) {
            throw new UnauthorizedException("Unauthorized!");
        }
        List<Patient> patients = patientService.findPatientsByDoctorIdAndStatus(doctor.getId(), "DONE");

        return ResponseEntity.ok().body(
                new DataResponse(
                        HttpStatus.OK.value(),
                        "danh sach benh nhan cua bac si: " + doctor.getName(),
                        patients.stream()
                                .map(patient ->
                                        CustomMap.of("name", patient.getName(),
                                                "gender", patient.getGender(),
                                                "address", patient.getAddress(),
                                                "status", patient.getStatus().getName(),
                                                "medical_records", patient.getMedicalRecord() == null ?
                                                        "" : CustomMap.of(
                                                        "basic_name", patient.getMedicalRecord().getName(),
                                                        "description", patient.getMedicalRecord().getDescription()
                                                )
                                        )
                                ).toList()
                )
        );

    }


    // admin thêm tài khoản bác sĩ
    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addDoctor(@Valid @RequestBody CreateDoctorRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity
                    .unprocessableEntity()
                    .body(
                            new ErrorResponse(
                                    HttpStatus.UNPROCESSABLE_ENTITY.value(),
                                    errors,
                                    HttpStatus.UNPROCESSABLE_ENTITY.name()
                            )
                    );
        }
        DoctorUser doctorUser = doctorUserService.create(request);
        return ResponseEntity.ok()
                .body(
                        new DataResponse(
                                HttpStatus.OK.value(),
                                "Create doctor user success!",
                                CustomMap.of(
                                        "doctor_id", doctorUser.getId(),
                                        "user", doctorUser.getUser() == null? null : CustomMap.of(
                                                "id", doctorUser.getUser().getId(),
                                                "name", doctorUser.getUser().getName(),
                                                "email", doctorUser.getUser().getEmail(),
                                                "address", doctorUser.getUser().getAddress(),
                                                "gender", doctorUser.getUser().getGender(),
                                                "phone", doctorUser.getUser().getPhone()
                                        ),
                                        "clinic", doctorUser.getClinic() == null ? null: CustomMap.of(
                                                "id", doctorUser.getClinic().getId(),
                                                "name", doctorUser.getClinic().getName()
                                        ),
                                        "specialization", doctorUser.getSpecialization() == null ? null: CustomMap.of(
                                                "id", doctorUser.getSpecialization().getId(),
                                                "name", doctorUser.getSpecialization().getName()
                                        ),
                                        "training_process", doctorUser.getProcess(),
                                        "introduce", doctorUser.getIntroduce(),
                                        "achievement", doctorUser.getAchievement()
                                )
                        )
                );
    }

    // khoá hoặc huỷ khoá tài khoản bác sĩ
    @PutMapping("/{doctorId}/lock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> lockOrUnlockDoctor(@PathVariable("doctorId") Long doctorId,
                                                @RequestBody Map<String, String> map) {
        String description = map.get("description");
        User doctor = userService.findUserById(doctorId)
                .orElseThrow(
                        () -> new NotFoundException("user not found")
                );
        if (!doctor.getRole().getName().equals("DOCTOR")) {
            throw new BadRequestException("user not doctor");
        }
        if (doctor.getIsActive() == 1) {
            doctor.setIsActive(0);
            doctor.setDescription(description);
        } else {
            doctor.setIsActive(1);
            doctor.setDescription(null);
        }
        userService.save(doctor);

        return ResponseEntity.ok()
                .body(new DataResponse(HttpStatus.OK.value(),
                                (doctor.getIsActive() == 0 ? "lock" : "unlock") + " success!",
                                new HashMap<String, String>() {{
                                    put("name", doctor.getName());
                                    if (doctor.getIsActive() == 0) {
                                        put("description", doctor.getDescription());
                                    }
                                }}
                        )
                );
    }

    // gửi mail kèm file bệnh án về mail bệnh nhân
    @PostMapping("/send-mail-to-patient/{patientId}")
    public ResponseEntity<?> sendMailForPatient(@PathVariable("patientId") Long patientId,
                                                @RequestParam("file") MultipartFile file) throws MessagingException, IOException {
        Patient patient = patientService.findById(patientId)
                .orElseThrow(
                        () -> new NotFoundException("patient not found")
                );
        if (patient.getStatus().getId() == 2 || patient.getStatus().getId() == 3) {
            throw new BadRequestException("cant send email for this patient");
        }
        if (file.isEmpty()) {
            throw new NotFoundException("file not found");
        }
        if (!file.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
            throw new BadRequestException("type not pdf");
        }
        String subject = "Medical Record of " + patient.getName();
        String body = "Thanks you for use our service!\n" +
                "Medical Examination Date: " + patient.getDateBooking() +
                "\nTime: " + patient.getTimeBooking() +
                "\nDoctor: " + patient.getDoctor().getName();
        doctorUserService.sendEmailforPatient(patient.getEmail(), subject, body, file);
        return ResponseEntity.ok(
                new DataResponse(HttpStatus.OK.value(),
                        "send records to " + patient.getName() + "'s email successfully!",
                        null));
    }


}
