package com.example.application.port.out;

import com.example.domain.model.entity.Member;
import com.example.domain.model.entity.Screening;
import com.example.domain.model.entity.Seat;
import java.util.List;

public interface ReservationValidationPort {
    void validateScreening(Screening screening);
    void validateMember(Member member);
    void validateRequestedSeatsExisted(List<Long> requestedSeatIds, List<Seat> foundSeats);
    void validateMaxSeatsPerScreening(int existingReservations, int newSeatCount);
    void validateSeatsAreAvailableForReservation(List<Seat> requestedSeats, List<Seat> alreadyReservedSeats);
    void validateSeatsAreConsecutive(List<Seat> seats);
}
