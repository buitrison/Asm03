package com.example.demo.dtos.request.schedules;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddScheduleRequest {
    @NotNull(message = "doctor id khong duoc trong!")
    @JsonProperty("doctor_id")
    private Long doctorId;
    @NotBlank(message = "date khong duoc trong!")
    private String date;
    @NotBlank(message = "time khong duoc trong!")
    private String time;
    @NotBlank(message = "max booking khong duoc trong!")
    @JsonProperty("max_booking")
    private String maxBooking;
    @NotBlank(message = "sum booking khong duoc trong!")
    @JsonProperty("sum_booking")
    private String sumBooking;
}
