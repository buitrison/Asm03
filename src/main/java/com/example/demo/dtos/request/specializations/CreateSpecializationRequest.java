package com.example.demo.dtos.request.specializations;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateSpecializationRequest {
    @NotBlank(message = "name khong duoc trong!")
    private String name;
    @NotBlank(message = "description khong duoc trong!")
    private String description;
    @NotBlank(message = "image khong duoc trong!")
    private String image;
}
