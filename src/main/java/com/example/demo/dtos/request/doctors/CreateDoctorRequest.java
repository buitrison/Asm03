package com.example.demo.dtos.request.doctors;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateDoctorRequest {
    @NotBlank(message = "name khong duoc trong!")
    private String name;
    @NotBlank(message = "email khong duoc trong!")
    private String email;
    @NotBlank(message = "password khong duoc trong!")
    private String password;
    @NotBlank(message = "address khong duoc trong!")
    private String address;
    @NotBlank(message = "phone khong duoc trong!")
    private String phone;
    @NotBlank(message = "avatar khong duoc trong!")
    private String avatar;
    @NotBlank(message = "gender khong duoc trong!")
    private String gender;
    @NotBlank(message = "introduce khong duoc trong!")
    private String introduce;
    @NotBlank(message = "process khong duoc trong!")
    private String process;
    @NotBlank(message = "achievement khong duoc trong!")
    private String achievement;
    @NotNull(message = "specialization id khong duoc trong!")
    @JsonProperty("specialization_id")
    private Long specializationId;
    @NotNull(message = "clinic id khong duoc trong!")
    @JsonProperty("clinic_id")
    private Long clinicId;
}
