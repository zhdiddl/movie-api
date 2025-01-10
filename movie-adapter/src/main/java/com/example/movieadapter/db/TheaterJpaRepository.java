package com.example.movieadapter.db;

import com.example.moviedomain.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TheaterJpaRepository extends JpaRepository<Theater, Long> {
}
