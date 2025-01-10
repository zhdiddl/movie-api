package com.example.movieadapter.db;

import com.example.moviedomain.entity.Screening;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScreeningJpaRepository extends JpaRepository<Screening, Long> {
}
