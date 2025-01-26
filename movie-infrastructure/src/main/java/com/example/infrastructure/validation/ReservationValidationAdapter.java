package com.example.infrastructure.validation;

import com.example.application.port.out.ReservationValidationPort;
import com.example.domain.exception.CustomException;
import com.example.domain.exception.ErrorCode;
import com.example.domain.model.entity.Seat;
import com.example.domain.validation.ReservationValidation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ReservationValidationAdapter implements ReservationValidationPort {

    private final ReservationValidation reservationValidation;

    @Override
    public void validateMaxSeatsPerScreening(int existingReservations, int newSeatCount) {
        reservationValidation.validateMaxSeatsPerScreening(existingReservations, newSeatCount);
    }

    @Override
    public void validateSeatsAreConsecutive(List<Seat> seats) {
        reservationValidation.validateSeatsAreConsecutive(seats);
    }

    @Override
    public void validateSeatsExist(List<Long> seatIds, List<Seat> requestedSeats) {
        if (requestedSeats.size() != seatIds.size()) {
            throw new CustomException(ErrorCode.INVALID_SEAT_NUMBER);
        }
    }

    @Override
    public void validateSeatsAreAvailableForReservation(List<Seat> requestedSeats, List<Seat> alreadyReservedSeats) {
        reservationValidation.validateSeatsAreAvailableForReservation(requestedSeats, alreadyReservedSeats);
    }


}
