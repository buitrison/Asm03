package com.example.demo.repositories;

import com.example.demo.models.DoctorUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DoctorUserRepository extends JpaRepository<DoctorUser, Long> {
}