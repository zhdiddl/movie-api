package com.example.moviedomain.converter;

import com.example.moviecommon.exception.CustomException;
import com.example.moviecommon.exception.ErrorCode;
import com.example.moviedomain.valueObject.ContentRating;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true) // 모든 ContentRating 필드에 자동으로 적용
public class ContentRatingConverter implements AttributeConverter<ContentRating, String> {

    @Override
    public String convertToDatabaseColumn(ContentRating contentRating) {
        if (contentRating == null) {
            throw new CustomException(ErrorCode.INVALID_CONTENT_RATING);
        }
        return contentRating.getDbValue();
    }

    @Override
    public ContentRating convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            throw new CustomException(ErrorCode.UNKNOWN_CONTENT_RATING);
        }
        return ContentRating.fromDbValue(dbData);
    }

}
