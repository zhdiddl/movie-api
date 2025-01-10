package com.example.moviedomain.entity;

import com.example.moviecommon.exception.CustomException;
import com.example.moviecommon.exception.ErrorCode;
import com.example.moviedomain.base.AuditingFields;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;

@Getter
@Entity
public class Screening extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Movie movie;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Theater theater;

    @Column(nullable = false)
    private LocalDate screeningDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;


    protected Screening() {}

    private Screening(Movie movie, Theater theater, LocalDate screeningDate, LocalTime startTime) {
        this.movie = movie;
        this.theater = theater;
        this.screeningDate = screeningDate;
        this.startTime = startTime;
        this.endTime = calculateEndTime(movie.getRuntimeMinutes());
    }

    public static Screening of(Movie movie, Theater theater, LocalDate screeningDate, LocalTime startTime) {
        validate(movie, theater, screeningDate, startTime);
        return new Screening(movie, theater, screeningDate, startTime);
    }

    private static void validate(Movie movie, Theater theater, LocalDate screeningDate, LocalTime startTime) {
        if (movie == null) {
            throw new CustomException(ErrorCode.INVALID_MOVIE);
        }
        if (theater == null) {
            throw new CustomException(ErrorCode.INVALID_THEATER);
        }
        if (screeningDate == null || startTime == null || screeningDate.isBefore(movie.getReleaseDate())) {
            throw new CustomException(ErrorCode.INVALID_SCREENING_TIME);
        }
    }

    private LocalTime calculateEndTime(int runtimeMinutes) {
        return this.startTime.plusMinutes(runtimeMinutes);
    }

}
