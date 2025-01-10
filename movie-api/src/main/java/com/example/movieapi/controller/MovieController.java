package com.example.movieapi.controller;

import com.example.movieapplication.dto.MovieResponseDto;
import com.example.movieapplication.port.in.MovieServicePort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieServicePort movieServicePort;

    @GetMapping
    public ResponseEntity<List<MovieResponseDto>> getAllMovies() {
        List<MovieResponseDto> movies = movieServicePort.getAllMovies();
        return ResponseEntity.ok(movies);
    }

}
