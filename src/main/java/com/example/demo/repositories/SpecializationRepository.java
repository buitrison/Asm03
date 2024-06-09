package com.example.demo.repositories;

import com.example.demo.models.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SpecializationRepository extends JpaRepository<Specialization, Long> {

    @Query("SELECT s " +
            "FROM Specialization s " +
            "GROUP BY s.id, s.name " +
            "ORDER BY (SELECT COUNT(p) FROM Patient p WHERE p.specialization = s) DESC")
    List<Specialization> findAllPopular();
}