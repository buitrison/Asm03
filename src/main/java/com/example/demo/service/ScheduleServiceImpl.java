package com.example.demo.service;

import com.example.demo.dtos.request.schedules.AddScheduleRequest;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.models.Schedule;
import com.example.demo.models.User;
import com.example.demo.repositories.ScheduleRepository;
import com.example.demo.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository,
                               UserRepository userRepository) {
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public List<Schedule> findScheduleByDoctorId(Long doctorId) {
        return scheduleRepository.findAllByDoctorId(doctorId);
    }

    @Override
    @Transactional
    public Schedule addSchedule(AddScheduleRequest request) {
        User doctor = userRepository.findById(request.getDoctorId())
                .orElseThrow(
                        () -> new NotFoundException("user not found")
                );
        if(!doctor.getRole().getName().equals("DOCTOR")){
            throw new BadRequestException("user id is not a doctor");
        }
        if(scheduleRepository.isScheduleAvailable(request.getDoctorId(), request.getDate(), request.getTime())){
            throw new BadRequestException("schedule adready registered");
        }
        Schedule schedule = Schedule.builder()
                .doctor(doctor)
                .date(request.getDate())
                .time(request.getTime())
                .maxBooking(request.getMaxBooking())
                .sumBooking(request.getSumBooking())
                .build();
        return scheduleRepository.save(schedule);
    }
}
