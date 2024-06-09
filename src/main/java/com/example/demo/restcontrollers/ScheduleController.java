package com.example.demo.restcontrollers;

import com.example.demo.dtos.request.schedules.AddScheduleRequest;
import com.example.demo.dtos.response.DataResponse;
import com.example.demo.dtos.response.ErrorResponse;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.models.Schedule;
import com.example.demo.models.User;
import com.example.demo.service.ScheduleService;
import com.example.demo.service.UserService;
import com.example.demo.utils.DateTimeUtills;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/schedules")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private UserService userService;

    // lấy lịch của bác sĩ
    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getScheduleOfDoctor(@PathVariable("doctorId") Long doctorId) {
        User doctor = userService.findUserById(doctorId)
                .orElseThrow(
                        () -> new NotFoundException("user not found")
                );
        if (!doctor.getRole().getName().equals("DOCTOR")) {
            throw new BadRequestException("id isnt doctor");
        }
        List<Schedule> schedules = scheduleService.findScheduleByDoctorId(doctorId);

        return ResponseEntity.ok()
                .body(
                        new DataResponse(
                                HttpStatus.OK.value(),
                                "list schedule of doctor " + doctor.getName(),
                                new LinkedHashMap<>() {{
                                    put("schedules", new ArrayList<>() {{
                                                for (Schedule schedule : schedules) {
                                                    add(new LinkedHashMap<>() {{
                                                        put("date", schedule.getDate());
                                                        put("time", schedule.getTime());
                                                        put("status", DateTimeUtills.isExpired(schedule.getDate(),
                                                                schedule.getTime()) ? "Expired" : "Active");
                                                    }});
                                                }
                                            }}
                                    );
                                }}

                        )
                );
    }

    // thêm lịch
    @PostMapping("")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> addSchedule(@Valid @RequestBody AddScheduleRequest request,
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
        Schedule schedule = scheduleService.addSchedule(request);

        return ResponseEntity.ok()
                .body(
                        new DataResponse(
                                HttpStatus.OK.value(),
                                "add schedule success",
                                new LinkedHashMap<>() {{
                                    put("doctor_name", schedule.getDoctor().getName());
                                    put("date", schedule.getDate());
                                    put("time", schedule.getTime());
                                }}
                        )
                );
    }

}
