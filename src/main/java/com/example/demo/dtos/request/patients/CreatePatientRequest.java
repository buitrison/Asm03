package com.example.demo.dtos.request.patients;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePatientRequest {
    @NotBlank(message = "doctor name khong duoc trong!")
    @JsonProperty("doctor_name")
    private String doctorName;
    @NotBlank(message = "date khong duoc trong!")
    private String date;
    @NotBlank(message = "time khong duoc trong!")
    private String time;
    @NotBlank(message = "price khong duoc trong!")
    private String price;
    @NotBlank(message = "date of birth khong duoc trong!")
    @JsonProperty("date_of_birth")
    private String dateOfBirth;
    @NotBlank(message = "reason khong duoc trong!")
    private String reason;
}
