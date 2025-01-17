package com.example.domain.converter;

import com.example.domain.model.valueObject.Genre;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class GenreConverter implements AttributeConverter<Genre, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Genre genre) {
        assert genre != null : "[ERROR] Genre can not be null";
        return genre.getDbValue();
    }

    @Override
    public Genre convertToEntityAttribute(Integer dbData) {
        assert dbData != null : "[ERROR] Genre can not be null";
        return Genre.fromDbValue(dbData);
    }
}
