package com.example.application.port.out;

import com.example.domain.model.entity.Seat;
import java.util.List;

public interface ReservationValidationPort {
    void validateMaxSeatsPerScreening(int existingReservations, int newSeatCount);
    void validateSeatsAreAvailableForReservation(List<Seat> requestedSeats, List<Seat> alreadyReservedSeats);
    void validateSeatsAreConsecutive(List<Seat> seats);
}
