package com.example.demo.restcontrollers;

import com.example.demo.dtos.request.patients.CreatePatientRequest;
import com.example.demo.dtos.response.DataResponse;
import com.example.demo.dtos.response.ErrorResponse;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.jwt.JwtTokenUtils;
import com.example.demo.models.Patient;
import com.example.demo.models.Status;
import com.example.demo.models.User;
import com.example.demo.repositories.StatusRepository;
import com.example.demo.service.PatientService;
import com.example.demo.service.UserService;
import com.example.demo.utils.CustomMap;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/${api.prefix}/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;
    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Autowired
    private StatusRepository statusRepository;

    // user book lịch bác sĩ
    @PostMapping("/book-by-user/{userId}")
    public ResponseEntity<?> addBookingPatient(@Valid @RequestBody CreatePatientRequest patientRequest,
                                               @PathVariable("userId") Long userId,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.unprocessableEntity()
                    .body(
                            new ErrorResponse(
                                    HttpStatus.UNPROCESSABLE_ENTITY.value(),
                                    errors,
                                    HttpStatus.UNPROCESSABLE_ENTITY.name()
                            )
                    );
        }

        User user = userService.findUserById(userId)
                .orElseThrow(
                        () -> new NotFoundException("user not found")
                );

        Patient patient = patientService.create(patientRequest, user);
        return ResponseEntity.ok()
                .body(
                        new DataResponse(
                                HttpStatus.OK.value(),
                                "add patient booking successfully!",
                                CustomMap.of(
                                        "name", patient.getName(),
                                        "phone", patient.getPhone(),
                                        "date_booking", patient.getDateBooking(),
                                        "time_booking", patient.getTimeBooking(),
                                        "doctor", patient.getDoctor() == null ? null : patient.getDoctor().getName(),
                                        "price", patient.getPrice(),
                                        "reason", patient.getReason(),
                                        "status", patient.getStatus() == null ? null : patient.getStatus().getName()

                                )
                        )
                );
    }


    // lấy danh sách bệnh nhân của bác sĩ x với trạng thái y (y gồm SUCCESS, CANCEL, DONE,...)
    @GetMapping("/doctor/{doctorId}/status/{status}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> getListPatientByDoctorIdAndStatus(@PathVariable("doctorId") Long doctorId,
                                                               @PathVariable("status") String status) {

        User doctor = userService.findUserById(doctorId)
                .orElseThrow(
                        () -> new NotFoundException("user not found!")
                );
        Status statusFound = statusRepository.findByName(status.toUpperCase())
                .orElseThrow(
                        () -> new NotFoundException("Status not found")
                );

//        // chỉ bác sĩ mới được xem danh sách của mình, không thể xem của bác sĩ khác
//        String email = jwtTokenUtils.extractEmail(token.substring(7));
//        if(!doctor.getEmail().equals(email)) {
//            throw new UnauthorizedException("truy cap tu choi!");
//        }

        List<Patient> patients = patientService.findPatientsByDoctorIdAndStatus(doctor.getId(), statusFound.getName());
        return ResponseEntity.ok()
                .body(
                        new DataResponse(
                                HttpStatus.OK.value(),
                                "Danh sach benh nhan " + status.toUpperCase(),
                                patients.stream()
                                        .map(patient ->
                                                CustomMap.of(
                                                        "id", patient.getId(),
                                                        "name", patient.getName(),
                                                        "gender", patient.getGender(),
                                                        "address", patient.getAddress(),
                                                        "status", patient.getStatus().getName(),
                                                        "medical_records", patient.getMedicalRecord() == null ?
                                                                null : CustomMap.of(
                                                                "basic_name", patient.getMedicalRecord().getName(),
                                                                "description", patient.getMedicalRecord().getDescription()
                                                        )
                                                )
                                        ).toList()
                        )
                );
    }

    // khoá hoặc huỷ khoá tài khoản bệnh nhân
    @PutMapping("/{userId}/lock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> lockOrUnlockDoctor(@PathVariable("userId") Long userId,
                                                @RequestBody Map<String, String> map
    ) {
        String description = map.get("description");
        User user = userService.findUserById(userId)
                .orElseThrow(
                        () -> new NotFoundException("user not found")
                );
        if (!user.getRole().getName().equals("USER")) {
            throw new BadRequestException("user not patient");
        }
        if (user.getIsActive() == 1) {
            user.setIsActive(0);
            user.setDescription(description);
        } else {
            user.setIsActive(1);
            user.setDescription(null);
        }
        userService.save(user);

        return ResponseEntity.ok()
                .body(
                        new DataResponse(
                                HttpStatus.OK.value(),
                                (user.getIsActive() == 0 ? "lock" : "unlock") + " success!",
                                new HashMap<String, String>() {{
                                    put("name", user.getName());
                                    if (user.getIsActive() == 0) {
                                        put("description", user.getDescription());
                                    }
                                }}
                        )
                );
    }


    // nhận lịch khám
    @PutMapping("/{patientId}/accept")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> applyPatientBooking(@PathVariable("patientId") Long patientId) {
        Patient patient = patientService.findById(patientId)
                .orElseThrow(
                        () -> new NotFoundException("patient is not found")
                );
        Patient finalPatient = patientService.updatePatientStatusFromPending(patient, "SUCCESS", null);
        return ResponseEntity.ok()
                .body(
                        new DataResponse(
                                HttpStatus.OK.value(),
                                "apply success!",
                                new LinkedHashMap<>() {{
                                    put("name", finalPatient.getName());
                                    put("date", finalPatient.getDateBooking());
                                    put("time", finalPatient.getTimeBooking());
                                    put("status", finalPatient.getStatus() == null? null : finalPatient.getStatus().getName());
                                }}
                        )
                );
    }

    // huỷ lịch khám
    @PutMapping("/{patientId}/cancel")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> cancelPatientBooking(@PathVariable("patientId") Long patientId,
                                                  @RequestBody Map<String, String> map) {
        Patient patient = patientService.findById(patientId)
                .orElseThrow(
                        () -> new NotFoundException("patient is not found")
                );
        Patient finalPatient = patientService
                .updatePatientStatusFromPending(patient, "CANCEL", map.get("description"));
        return ResponseEntity.ok()
                .body(
                        new DataResponse(
                                HttpStatus.OK.value(),
                                "cancelled success!",
                                new LinkedHashMap<>() {{
                                    put("name", finalPatient.getName());
                                    put("status", finalPatient.getStatus() == null? null : finalPatient.getStatus().getName());
                                    put("reason", finalPatient.getDescription());
                                }}
                        )
                );
    }

    // admin lấy thông tin lịch khám của bệnh nhân
    @GetMapping("/{userId}/schedules")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getPatientSchedules(@PathVariable("userId") Long userId) {
        User user = userService.findUserById(userId)
                .orElseThrow(
                        () -> new NotFoundException("user not found")
                );
        if (!user.getRole().getName().equals("USER")) {
            throw new BadRequestException("user not patient");
        }
        List<Patient> patients = patientService.findByEmail(user.getEmail());
        return ResponseEntity.ok()
                .body(
                        new DataResponse(
                                HttpStatus.OK.value(),
                                "schedules of user id " + user.getId(),
                                new ArrayList<>() {{
                                    for (Patient patient : patients) {
                                        add(
                                                new LinkedHashMap<>() {{
                                                    put("date", patient.getDateBooking());
                                                    put("time", patient.getTimeBooking());
                                                    put("status", patient.getStatus() == null? null : patient.getStatus().getName());
                                                }}
                                        );
                                    }
                                }}
                        )
                );
    }

}
