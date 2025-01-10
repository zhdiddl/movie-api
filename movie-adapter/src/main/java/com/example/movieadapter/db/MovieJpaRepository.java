package com.example.movieadapter.db;

import com.example.moviedomain.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieJpaRepository extends JpaRepository<Movie, Long> {
}
