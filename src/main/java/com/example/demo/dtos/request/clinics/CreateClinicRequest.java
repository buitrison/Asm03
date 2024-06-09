package com.example.demo.dtos.request.clinics;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateClinicRequest {
    @NotBlank(message = "name khong duoc trong!")
    private String name;
    @NotBlank(message = "address khong duoc trong!")
    private String address;
    @NotBlank(message = "phone khong duoc trong!")
    private String phone;
    @NotBlank(message = "intro html khong duoc trong!")
    @JsonProperty("introduction_html")
    private String introductionHTML;
    @NotBlank(message = "intro markdown khong duoc trong!")
    @JsonProperty("introduction_markdown")
    private String introductionMarkdown;
    @NotBlank(message = "description khong duoc trong!")
    private String description;
    @NotBlank(message = "image khong duoc trong!")
    private String image;

}
