package com.example.application.port.out;

import com.example.domain.model.entity.Member;
import com.example.domain.model.entity.Screening;
import com.example.domain.model.entity.Seat;
import com.example.domain.model.entity.SeatReservation;
import java.util.List;

public interface SeatReservationRepositoryPort {
    int countByScreeningAndReservationMember(Screening screening, Member member);
    List<Seat> findReservedSeatsByScreening(Screening screening);
    void save(SeatReservation seatReservation);
    void deleteAll();
    long count();
}
