package com.example.demo.repositories;

import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);;

    boolean existsByEmail(String email);

    User save(User user);

    @Query("SELECT u FROM User u WHERE u.name =:name")
    Optional<User> findByName(@Param("name") String name);
}
