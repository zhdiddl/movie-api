package com.example.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    INVALID_SEAT_NUMBER("Seat row must be between A-E and column must be between 1-5.", 400),
    INVALID_TITLE("Movie title cannot be empty.", 400),
    INVALID_CONTENT_RATING( "Content rating must be provided.", 400),
    INVALID_RELEASE_DATE("Release date cannot be earlier than today.", 400),
    INVALID_RUNTIME("Running time must be greater than zero.", 400),
    INVALID_GENRE("Genre cannot be empty.", 400),
    INVALID_MOVIE("Movie must be provided.", 400),
    INVALID_THEATER("Theater must be provided.", 400),
    INVALID_SCREENING_TIME("Screening date and time cannot be null or earlier than the release date.", 400),
    INVALID_THEATER_NAME("Theater name cannot be empty.", 400),

    UNKNOWN_CONTENT_RATING("Unknown database value for ContentRating", 400),

    RESOURCE_NOT_FOUND("Resource not found", 404),

    INTERNAL_SERVER_ERROR("An internal server error occurred", 500),
    ;

    private final String message;
    private final int statusCode;

}
