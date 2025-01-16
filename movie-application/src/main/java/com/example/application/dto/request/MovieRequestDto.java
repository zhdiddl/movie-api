package com.example.application.dto.request;

import com.example.domain.model.valueObject.ContentRating;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record MovieRequestDto(

        @NotNull(message = "Title must not be null")
        @Size(min = 1, message = "Title must not be empty")
        String title,

        @NotNull(message = "Content rating must not be null")
        ContentRating contentRating,

        @NotNull(message = "Release date must not be null")
        LocalDate releaseDate,

        @Size(min = 1, message = "Thumbnail URL must not be empty")
        String thumbnailUrl,

        @NotNull(message = "Runtime must not be null")
        int runtimeMinutes,

        @NotNull(message = "Genre must not be null")
        @Size(min = 1, message = "Genre must not be empty")
        String genre

) {}
