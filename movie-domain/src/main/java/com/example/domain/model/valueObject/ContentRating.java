package com.example.domain.model.valueObject;

import java.util.Arrays;
import java.util.Optional;
import lombok.Getter;

@Getter
public enum ContentRating {

    ALL("전체 관람가", 0, "A"),
    TWELVE("12세 이상 관람가", 12, "B"),
    FIFTEEN("15세 이상 관람가", 15, "C"),
    ADULT("청소년 관람 불가", 18, "D"),
    UNKNOWN("알 수 없는 등급", 0, "Z"),
    ;

    private final String description;
    private final int ageLimit;
    private final String dbValue;

    ContentRating(String description, int ageLimit, String dbValue) {
        this.description = description;
        this.ageLimit = ageLimit;
        this.dbValue = dbValue;
    }

    public static Optional<ContentRating> fromDbValue(String dbValue) {
        if (dbValue == null) {
            return Optional.empty();
        }
        return Arrays.stream(values())
                .filter(contentRating -> contentRating.getDbValue().equals(dbValue))
                .findFirst();
    }

}
