package com.example.demo.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank(message = "email khong duoc trong!")
    private String email;
    @NotBlank(message = "password khong duoc trong!")
    private String password;
}
