package com.example.demo.restcontrollers;

import com.example.demo.dtos.request.clinics.CreateClinicRequest;
import com.example.demo.dtos.response.DataResponse;
import com.example.demo.dtos.response.ErrorResponse;
import com.example.demo.models.Clinic;
import com.example.demo.service.ClinicService;
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
@RequestMapping("${api.prefix}/clinics")
public class ClinicController {

    @Autowired
    private ClinicService clinicService;

    // hiển thị những cơ sở y tế nổi bật
    @GetMapping("/popular")
    public ResponseEntity<?> getPopulerClinics() {
        List<Clinic> clinics = clinicService.findAllPopular();
        return ResponseEntity.ok(
                new DataResponse(
                        HttpStatus.OK.value(),
                        "danh sach phong kham noi bat (nhieu apply)",
                        new ArrayList<>() {{
                            for (Clinic clinic : clinics) {

                                add(
                                        new LinkedHashMap<>() {{
                                            put("id", clinic.getId());
                                            put("name", clinic.getName());
                                            put("address", clinic.getAddress());
                                            put("time", clinic.getTime());
                                            put("fee", clinic.getFee());
                                            put("description", clinic.getDescription());
                                        }}
                                );
                            }
                        }}
                )
        );
    }

    // thêm cơ sở y tế
    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createClinic(@Valid @RequestBody CreateClinicRequest clinicRequest,
                                          BindingResult bindingResult) {
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

        Clinic clinic = clinicService.create(clinicRequest);

        return ResponseEntity.ok(new DataResponse(
                HttpStatus.OK.value(),
                "Create successfully!",
                clinic
        ));
    }


}
