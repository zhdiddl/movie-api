package com.example.application.dto.response;

import com.example.domain.model.entity.Screening;
import com.example.domain.model.projection.ScreeningProjection;
import java.time.LocalTime;

public record ScreeningResponseDto(
        LocalTime startTime,
        LocalTime endTime
) {
    public static ScreeningResponseDto fromProjection(ScreeningProjection screening) {
        return new ScreeningResponseDto(
                screening.getStartTime(),
                screening.getEndTime()
        );
    }

    public static ScreeningResponseDto fromEntity(Screening screening) {
        return new ScreeningResponseDto(
                screening.getStartTime(),
                screening.getEndTime()
        );
    }
}
