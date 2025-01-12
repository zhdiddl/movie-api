package com.example.movieadapter.db;

import com.example.moviedomain.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieJpaRepository extends JpaRepository<Movie, Long> {
}
