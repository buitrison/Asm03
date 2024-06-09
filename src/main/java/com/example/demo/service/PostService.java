package com.example.demo.service;

import com.example.demo.models.Post;

import java.util.List;

public interface PostService {

    List<Post> findAll();

    List<Post> findBySpecialization(String specializationName, String keyword);

    List<Post> findByRegionCategoryPriceClinic(String region, String category, Long minPrice, Long maxPrice, String clinic, String keyword);
}
