package com.example.movieadapter.db;

import com.example.movieapplication.port.out.MovieRepositoryPort;
import com.example.moviedomain.entity.Movie;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * This adapter serves as a bridge between the application layer
 * and the database layer, allowing the application to access movie
 * data through the defined MovieRepositoryPort interface while
 * encapsulating the JPA-specific implementation details.
 */

@RequiredArgsConstructor
@Repository
public class MovieJpaRepositoryAdapter implements MovieRepositoryPort {

    private final MovieJpaRepository movieJpaRepository;

    @Override
    public List<Movie> findAll() {
        return movieJpaRepository.findAll();
    }

}
