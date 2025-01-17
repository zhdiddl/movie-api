package com.example.domain.model.valueObject;

import com.example.domain.exception.CustomException;
import com.example.domain.exception.ErrorCode;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;

@Getter
public enum Genre {

    THRILLER(1),
    ACTION(2),
    DRAMA(3),
    COMEDY(4),
    SCI_FI(5),
    ;

    private final int dbValue;

    Genre(int dbValue) {
        this.dbValue = dbValue;
    }

    private static final Map<Integer, Genre> MAP =
            Stream.of(values()).collect(Collectors.toMap(Genre::getDbValue, genre -> genre));

    public static Genre fromDbValue(int dbValue) {
        Genre genre = MAP.get(dbValue);
        if (genre == null) {
            throw new CustomException(ErrorCode.INVALID_GENRE);
        }
        return genre;
    }

}
