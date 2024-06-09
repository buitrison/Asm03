package com.example.demo.dtos.request.users;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    @NotBlank(message = "name khong duoc trong!")
    private String name;
    @NotBlank(message = "address khong duoc trong!")
    private String address;
    @NotBlank(message = "phone khong duoc trong!")
    private String phone;
    @NotBlank(message = "gender khong duoc trong!")
    private String gender;
    @NotBlank(message = "avatar khong duoc trong!")
    private String avatar;
}
