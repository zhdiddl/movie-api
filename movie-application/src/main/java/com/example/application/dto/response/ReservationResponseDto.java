package com.example.application.dto.response;

import com.example.domain.model.entity.Reservation;
import java.time.LocalTime;
import java.util.List;

public record ReservationResponseDto(
        String movieTitle,
        String theaterName,
        LocalTime screeningStartTime,
        List<String> reservedSeats
) {
    public static ReservationResponseDto fromEntity(Reservation reservation) {
        return new ReservationResponseDto(
                reservation.getScreening().getMovie().getTitle(),
                reservation.getScreening().getTheater().getName(),
                reservation.getScreening().getStartTime(),
                reservation.getSeatReservations().stream()
                        .map(seatReservation -> seatReservation.getSeat().getSeatNumber().toString())
                        .toList()
        );
    }
}

