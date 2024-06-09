package com.example.demo.restcontrollers;

import com.example.demo.dtos.request.users.UpdateUserRequest;
import com.example.demo.dtos.response.DataResponse;
import com.example.demo.dtos.response.ErrorResponse;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.models.Patient;
import com.example.demo.models.User;
import com.example.demo.service.PatientService;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private PatientService patientService;

    // hiển thị thông tin user cùng lịch sử khám chữa
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable("userId") Long userId) {
        User user = userService.findUserById(userId)
                .orElseThrow(
                        () -> new NotFoundException("user not found")
                );
        List<Patient> patients = patientService.findByEmail(user.getEmail());

        return ResponseEntity.ok(
                new DataResponse(
                        HttpStatus.OK.value(),
                        "Info of user: " + user.getName(),
                        new LinkedHashMap<>() {{
                            put("id", user.getId());
                            put("name", user.getName());
                            put("gender", user.getGender());
                            put("email", user.getEmail());
                            put("address", user.getAddress());
                            put("phone", user.getPhone());
                            put("role", user.getRole() == null ? null : user.getRole().getName());
                            put("medical_history", patients.isEmpty() ? null : new ArrayList<>() {{

                                        for (Patient patient : patients) {

                                            add(
                                                    new LinkedHashMap<>() {{
                                                        put("id", patient.getId());
                                                        put("doctor",
                                                                patient.getDoctor() == null ?
                                                                        null : patient.getDoctor().getName()
                                                        );
                                                        put("specialization",
                                                                patient.getSpecialization() == null ?
                                                                        null : patient.getSpecialization().getName()
                                                        );
                                                        put("clinic",
                                                                patient.getClinic() == null ?
                                                                        null : patient.getClinic().getName()
                                                        );
                                                        put("date_booking", patient.getDateBooking());
                                                        put("time_booking", patient.getTimeBooking());
                                                        put("reason", patient.getReason());
                                                        put("status",
                                                                patient.getStatus() == null ?
                                                                        null : patient.getStatus().getName()
                                                        );

                                                    }}
                                            );


                                        }
                                    }}
                            );

                        }}
                ));

    }

    // cập nhật user với user id
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(
            @PathVariable("userId") Long userId,
            @Valid @RequestBody UpdateUserRequest updateUserRequest,
            BindingResult bindingResult
    ) {
        User user = userService.findUserById(userId)
                .orElseThrow(
                        () -> new NotFoundException("user not found")
                );

        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.unprocessableEntity().body(
                    new ErrorResponse(
                            HttpStatus.UNPROCESSABLE_ENTITY.value(),
                            errorMessages,
                            HttpStatus.UNPROCESSABLE_ENTITY.name()
                    )
            );
        }
        User newUser = userService.updateUser(user, updateUserRequest);

        return ResponseEntity.ok(
                new DataResponse(
                        HttpStatus.ACCEPTED.value(),
                        "update user success",
                        new LinkedHashMap<>(){{
                            put("id", user.getId());
                            put("name", newUser.getName());
                            put("gender", newUser.getGender());
                            put("address", newUser.getAddress());
                            put("phone", newUser.getPhone());
                            put("avatar", newUser.getAvatar());
                            put("role", newUser.getRole() == null ? null : newUser.getRole().getName());
                        }}

                )
        );

    }


}
