package com.example.infrastructure.web;

import com.example.application.dto.request.MovieRequestDto;
import com.example.application.dto.response.MovieResponseDto;
import com.example.application.port.in.MovieServicePort;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {

    private final MovieServicePort movieServicePort;

    @GetMapping
    public ResponseEntity<List<MovieResponseDto>> getAllMovies() {
        List<MovieResponseDto> movies = movieServicePort.getMoviesSortedByReleaseDateDesc();
        return ResponseEntity.ok(movies);
    }

    @PostMapping
    public ResponseEntity<Void> createMovie(@RequestBody @Valid MovieRequestDto movieRequestDto) {
        movieServicePort.createMovie(movieRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
