package com.example.demo.dtos.request.users;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SendMailRequest {
    @NotBlank(message = "email khong duoc trong!")
    private String email;
}
