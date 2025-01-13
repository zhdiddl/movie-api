package com.example.moviedomain.valueObject;

import com.example.moviecommon.exception.CustomException;
import com.example.moviecommon.exception.ErrorCode;
import lombok.Getter;

@Getter
public enum ContentRating {

    ALL("전체 관람가", 0, "A"),
    TWELVE("12세 이상 관람가", 12, "B"),
    FIFTEEN("15세 이상 관람가", 15, "C"),
    ADULT("청소년 관람 불가", 18, "D");

    private final String description;
    private final int ageLimit;
    private final String dbValue;

    ContentRating(String description, int ageLimit, String dbValue) {
        this.description = description;
        this.ageLimit = ageLimit;
        this.dbValue = dbValue;
    }

    public static ContentRating fromDbValue(String dbValue) {
        for (ContentRating contentRating : values()) {
            if (contentRating.getDbValue().equals(dbValue)) {
                return contentRating;
            }
        }
        throw new CustomException(ErrorCode.UNKNOWN_CONTENT_RATING);
    }

}
