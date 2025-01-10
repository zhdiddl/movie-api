package com.example.movieapplication.port.in;

import com.example.movieapplication.dto.MovieResponseDto;
import java.util.List;

public interface MovieServicePort {
    List<MovieResponseDto> getAllMovies();
}
