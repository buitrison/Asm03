package com.example.demo.repositories;

import com.example.demo.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword% AND p.specialization.name = :specializationName")
    List<Post> findBySpecializationId(@Param("specializationName") String specializationName,
                                      @Param("keyword") String keyword);


    @Query("SELECT p FROM Post p WHERE " +
            "p.title LIKE %:keyword% AND " +
            "(:region IS NULL OR p.address = :region) AND " +
            "(:category IS NULL OR p.category.name = :category) AND " +
            "(:minPrice IS NULL OR p.fee >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.fee <= :maxPrice) AND " +
            "(:clinic IS NULL OR p.clinic.name = :clinic)")
    List<Post> findByRegionCategoryPriceClinic(@Param("region") String region,
                                               @Param("category") String category,
                                               @Param("minPrice") Long minPrice,
                                               @Param("maxPrice") Long maxPrice,
                                               @Param("clinic") String clinic,
                                               @Param("keyword") String keyword);
}