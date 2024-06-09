package com.example.demo.repositories;

import com.example.demo.models.Extrainfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExtrainfoRepository extends JpaRepository<Extrainfo, Long> {
}