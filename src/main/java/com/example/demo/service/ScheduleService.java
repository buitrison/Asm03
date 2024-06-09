package com.example.demo.service;

import com.example.demo.dtos.request.schedules.AddScheduleRequest;
import com.example.demo.models.Schedule;

import java.util.List;

public interface ScheduleService {

    List<Schedule> findScheduleByDoctorId(Long doctorId);

    Schedule addSchedule(AddScheduleRequest request);
}
