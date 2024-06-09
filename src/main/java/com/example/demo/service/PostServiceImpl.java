package com.example.demo.service;

import com.example.demo.models.Post;
import com.example.demo.repositories.PostRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;


    @Override
    @Transactional
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    @Transactional
    public List<Post> findBySpecialization(String specializationName, String keyword) {
        return postRepository.findBySpecializationId(specializationName, keyword);
    }

    @Override
    @Transactional
    public List<Post> findByRegionCategoryPriceClinic(String region,
                                                      String category,
                                                      Long minPrice,
                                                      Long maxPrice,
                                                      String clinic,
                                                      String keyword) {
        return postRepository.findByRegionCategoryPriceClinic(region,category, minPrice, maxPrice, clinic, keyword);
    }
}
