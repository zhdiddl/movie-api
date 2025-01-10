package com.example.movieapplication.port.out;

import com.example.moviedomain.entity.Movie;
import java.util.List;

/**
 * Represents a port for accessing movie data from a persistence layer.
 * This interface provides an abstraction that allows the application layer
 * to interact with various repository implementations
 * (e.g., databases, external APIs).
 */
public interface MovieRepositoryPort {
    List<Movie> findAllMovies();
}
