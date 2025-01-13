package com.example.moviedomain.entity;

import com.example.moviecommon.exception.CustomException;
import com.example.moviecommon.exception.ErrorCode;
import com.example.moviedomain.base.AuditingFields;
import com.example.moviedomain.converter.ContentRatingConverter;
import com.example.moviedomain.valueObject.ContentRating;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
public class Movie extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Convert(converter = ContentRatingConverter.class)
    @Column(nullable = false)
    private ContentRating contentRating;

    @Column(nullable = false)
    private LocalDate releaseDate;

    @Setter private String thumbnailUrl;
    @Setter private int runtimeMinutes;
    @Setter @Column(nullable = false) private String genre;

    @OneToMany(mappedBy = "movie", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Screening> screenings = new ArrayList<>();


    protected Movie() {}

    private Movie(String title,
                  ContentRating contentRating,
                  LocalDate releaseDate,
                  String thumbnailUrl,
                  int runtimeMinutes,
                  String genre) {
        this.title = title;
        this.contentRating = contentRating;
        this.releaseDate = releaseDate;
        this.thumbnailUrl = thumbnailUrl;
        this.runtimeMinutes = runtimeMinutes;
        this.genre = genre;
    }

    public static Movie of(String title,
                           ContentRating contentRating,
                           LocalDate releaseDate,
                           String thumbnailUrl,
                           int runtimeMinutes,
                           String genre) {
        validate(title, contentRating, releaseDate, runtimeMinutes, genre);
        return new Movie(title, contentRating, releaseDate, thumbnailUrl, runtimeMinutes, genre);
    }

    private static void validate(String title,
                                 ContentRating contentRating,
                                 LocalDate releaseDate,
                                 int runtimeMinutes,
                                 String genre) {
        if (title == null || title.trim().isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_TITLE);
        }

        if (contentRating == null) {
            throw new CustomException(ErrorCode.INVALID_CONTENT_RATING);
        }

        if (releaseDate == null || releaseDate.isAfter(LocalDate.now())) {
            throw new CustomException(ErrorCode.INVALID_RELEASE_DATE);
        }

        if (runtimeMinutes <= 0) {
            throw new CustomException(ErrorCode.INVALID_RUNTIME);
        }

        if (genre == null || genre.trim().isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_GENRE);
        }
    }

}
