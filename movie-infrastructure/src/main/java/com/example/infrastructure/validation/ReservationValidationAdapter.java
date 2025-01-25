package com.example.infrastructure.validation;

import com.example.application.port.out.ReservationValidationPort;
import com.example.domain.model.entity.Member;
import com.example.domain.model.entity.Screening;
import com.example.domain.model.entity.Seat;
import com.example.domain.validation.MemberValidation;
import com.example.domain.validation.ReservationValidation;
import com.example.domain.validation.ScreeningValidation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ReservationValidationAdapter implements ReservationValidationPort {

    private final ScreeningValidation screeningValidation;
    private final MemberValidation memberValidation;
    private final ReservationValidation reservationValidation;

    @Override
    public void validateScreening(Screening screening) {
        screeningValidation.validateScreening(screening);
    }

    @Override
    public void validateMember(Member member) {
        memberValidation.validateMember(member);
    }

    @Override
    public void validateRequestedSeatsExisted(List<Long> requestedSeatIds, List<Seat> foundSeats) {
        reservationValidation.validateRequestedSeatsExisted(requestedSeatIds, foundSeats);
    }

    @Override
    public void validateMaxSeatsPerScreening(int existingReservations, int newSeatCount) {
        reservationValidation.validateMaxSeatsPerScreening(existingReservations, newSeatCount);
    }

    @Override
    public void validateSeatsAreConsecutive(List<Seat> seats) {
        reservationValidation.validateSeatsAreConsecutive(seats);
    }

    @Override
    public void validateSeatsAreAvailableForReservation(List<Seat> requestedSeats, List<Seat> alreadyReservedSeats) {
        reservationValidation.validateSeatsAreAvailableForReservation(requestedSeats, alreadyReservedSeats);
    }

}
