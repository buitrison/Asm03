package com.example.demo.restcontrollers;

import com.example.demo.dtos.request.specializations.CreateSpecializationRequest;
import com.example.demo.dtos.response.DataResponse;
import com.example.demo.dtos.response.ErrorResponse;
import com.example.demo.models.Specialization;
import com.example.demo.service.SpecializationService;
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
@RequestMapping("${api.prefix}/specializations")
public class SpecializationController {

    @Autowired
    private SpecializationService specializationService;

    // lấy ra thông tin những chuyên khoa nổi bật
    @GetMapping("/popular")
    public ResponseEntity<?> getSpecializationsPopular() {
        List<Specialization> specializations = specializationService.findAllPopular();
        return ResponseEntity.ok(
                new DataResponse(
                        HttpStatus.OK.value(),
                        "chuyen khoa noi bat",
                        new ArrayList<>() {{
                            for (Specialization specialization : specializations) {
                                add(new LinkedHashMap<>() {{
                                        put("id", specialization.getId());
                                        put("name", specialization.getName());
                                        put("description", specialization.getDescription());
                                        put("image", specialization.getImage());
                                    }}
                                );
                            }
                        }}
                )
        );
    }

    // thêm chuyên khoa
    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createSpecialization(@Valid @RequestBody CreateSpecializationRequest specializationRequest,
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

        Specialization specialization = specializationService.create(specializationRequest);

        return ResponseEntity.ok(new DataResponse(
                HttpStatus.OK.value(),
                "Create successfully!",
                specialization
        ));
    }


}
