package com.example.demo.dtos.request.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResetPasswordRequest {
    @NotBlank(message = "password khong duoc trong!")
    private String password;
    @NotBlank(message = "confirm password khong duoc trong!")
    @JsonProperty("confirm_password")
    private String confirmPassword;
}
