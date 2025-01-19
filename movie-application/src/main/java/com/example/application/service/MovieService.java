package com.example.application.service;

import com.example.application.dto.request.MovieRequestDto;
import com.example.application.dto.request.ScreeningRequestDto;
import com.example.application.dto.response.MovieResponseDto;
import com.example.application.port.in.MovieServicePort;
import com.example.application.port.out.MovieRepositoryPort;
import com.example.application.port.out.ScreeningRepositoryPort;
import com.example.application.port.out.TheaterRepositoryPort;
import com.example.domain.exception.CustomException;
import com.example.domain.exception.ErrorCode;
import com.example.domain.model.entity.Movie;
import com.example.domain.model.entity.Screening;
import com.example.domain.model.entity.Theater;
import com.example.domain.model.valueObject.Genre;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MovieService implements MovieServicePort {

    private final MovieRepositoryPort movieRepositoryPort;
    private final ScreeningRepositoryPort screeningRepositoryPort;
    private final TheaterRepositoryPort theaterRepositoryPort;

    private final Logger logger = LoggerFactory.getLogger(MovieService.class);

    @Cacheable(value = "movies", key = "#title + '-' + #genre")
    @Override
    public List<MovieResponseDto> findMovies(String title, Genre genre) {

        logger.info("🔥 캐시 메서드 실행됨! title={}, genre={}", title, genre);
        Sort sort = Sort.by("releaseDate").descending();

        return movieRepositoryPort.findBy(title, genre, sort).stream()
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

        movieRepositoryPort.save(movie);
    }

    @Override
    public void addScreeningToMovie(Long movieId, ScreeningRequestDto screeningRequestDto) {
        Movie movie = movieRepositoryPort.findById(movieId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_MOVIE));

        Theater theater = theaterRepositoryPort.findById(screeningRequestDto.theaterId())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_THEATER));

        Screening screening = Screening.of(
                screeningRequestDto.startTime(),
                movie,
                theater
        );

        screeningRepositoryPort.save(screening);
    }

}
