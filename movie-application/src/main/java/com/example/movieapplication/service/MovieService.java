package com.example.movieapplication.service;

import com.example.movieapplication.dto.MovieResponseDto;
import com.example.movieapplication.port.in.MovieServicePort;
import com.example.movieapplication.port.out.MovieRepositoryPort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MovieService implements MovieServicePort {

    private final MovieRepositoryPort movieRepositoryPort;

    @Override
    public List<MovieResponseDto> getAllMovies() {
        return movieRepositoryPort.findAll().stream()
                .map(MovieResponseDto::fromEntity)
                .toList();
    }

}
