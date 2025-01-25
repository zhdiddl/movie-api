package com.example.infrastructure.persistence;

import com.example.application.port.out.SeatReservationRepositoryPort;
import com.example.domain.model.entity.Member;
import com.example.domain.model.entity.Screening;
import com.example.domain.model.entity.Seat;
import com.example.domain.model.entity.SeatReservation;
import com.example.infrastructure.db.SeatReservationJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class SeatReservationJpaRepositoryAdapter implements SeatReservationRepositoryPort {

    private final SeatReservationJpaRepository seatReservationJpaRepository;

    @Override
    public int countByScreeningAndReservationMember(Screening screening, Member member) {
        return seatReservationJpaRepository.countByScreeningAndReservation_Member(screening, member);
    }

    @Override
    public List<Seat> findReservedSeatsByScreening(Screening screening) {
        return seatReservationJpaRepository.findReservedSeatsByScreening(screening);
    }

    @Override
    public void save(SeatReservation seatReservation) {
        seatReservationJpaRepository.save(seatReservation);
    }

}
