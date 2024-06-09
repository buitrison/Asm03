package com.example.demo.repositories;

import com.example.demo.models.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Schedule s WHERE s.doctor.id = :doctorId AND s.date =:date AND s.time = :time")
    boolean isScheduleAvailable(@Param("doctorId") Long doctorId, @Param("date") String date, @Param("time") String time);

    @Query("SELECT s FROM Schedule s WHERE s.doctor.id =:doctorId")
    List<Schedule> findAllByDoctorId(@Param("doctorId") Long doctorId);

    @Query("SELECT s FROM Schedule s WHERE s.doctor.id =:doctorId AND s.date =:date AND s.time = :time")
    Optional<Schedule> findByDateAndTime(Long doctorId, String date, String time);
}