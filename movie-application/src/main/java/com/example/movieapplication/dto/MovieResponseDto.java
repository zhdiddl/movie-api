package com.example.movieapplication.dto;

import com.example.moviedomain.entity.Movie;
import java.time.LocalDate;
import java.util.List;

public record MovieResponseDto(
        Long id,
        String title,
        String contentRating,
        LocalDate releaseDate,
        String thumbnailUrl,
        int runtimeMinutes,
        String genre,
        List<ScreeningResponseDto> screenings
) {
    public static MovieResponseDto fromEntity(Movie movie) {
        return new MovieResponseDto(
                movie.getId(),
                movie.getTitle(),
                movie.getContentRating().name(),
                movie.getReleaseDate(),
                movie.getThumbnailUrl(),
                movie.getRuntimeMinutes(),
                movie.getGenre(),
                movie.getScreenings().stream()
                        .map(ScreeningResponseDto::fromEntity)
                        .toList()
        );
    }
}
