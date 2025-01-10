package com.example.moviedomain.valueObject;

import lombok.Getter;

@Getter
public enum ContentRating {

    ALL("전체 관람가", 0),
    TWELVE("12세 이상 관람가", 12),
    FIFTEEN("15세 이상 관람가", 15),
    ADULT("청소년 관람 불가", 18);

    private final String description;
    private final int ageLimit;

    ContentRating(String description, int ageLimit) {
        this.description = description;
        this.ageLimit = ageLimit;
    }

}
