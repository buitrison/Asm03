package com.example.demo.repositories;

import com.example.demo.models.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClinicRepository extends JpaRepository<Clinic, Long> {
    @Query("SELECT c " +
            "FROM Clinic c " +
            "GROUP BY c.id, c.name " +
            "ORDER BY (SELECT COUNT(p) FROM Patient p WHERE p.clinic = c) DESC")
    List<Clinic> findAllPopular();
}