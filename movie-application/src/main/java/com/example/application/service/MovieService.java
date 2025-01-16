package com.example.application.service;

import com.example.application.dto.request.MovieRequestDto;
import com.example.application.dto.response.MovieResponseDto;
import com.example.application.port.in.MovieServicePort;
import com.example.application.port.out.MovieRepositoryPort;
import com.example.domain.model.entity.Movie;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MovieService implements MovieServicePort {

    private final MovieRepositoryPort movieRepositoryPort;

    @Override
    public List<MovieResponseDto> getMoviesSortedByReleaseDateDesc() {
        Sort sort = Sort.by("releaseDate").descending();

        return movieRepositoryPort.findAll(sort).stream()
                .map(MovieResponseDto::fromEntity)
                .toList();
    }

    @Override
    public void createMovie(MovieRequestDto movieRequestDto) {
        Movie movie = Movie.of(movieRequestDto.title(),
                movieRequestDto.contentRating(),
                movieRequestDto.releaseDate(),
                movieRequestDto.thumbnailUrl(),
                movieRequestDto.runtimeMinutes(),
                movieRequestDto.genre());

        movieRepositoryPort.createMovie(movie);
    }

}
