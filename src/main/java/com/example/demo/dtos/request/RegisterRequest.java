package com.example.demo.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "name khong duoc trong!")
    private String name;

    @NotBlank(message = "gender khong duoc trong!")
    private String gender;

    @NotBlank(message = "email khong duoc trong!")
    private String email;

    @NotBlank(message = "phone khong duoc trong!")
    private String phone;

    @NotBlank(message = "address khong duoc trong!")
    private String address;

    @NotBlank(message = "password khong duoc trong!")
    private String password;

    @NotBlank(message = "confirm password khong duoc trong!")
    @JsonProperty("confirm_password")
    private String confirmPassword;
}
