package com.example.infrastructure.db;

import com.example.domain.model.entity.Member;
import com.example.domain.model.entity.Screening;
import com.example.domain.model.entity.Seat;
import com.example.domain.model.entity.SeatReservation;
import jakarta.persistence.LockModeType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface SeatReservationJpaRepository extends JpaRepository<SeatReservation, Long> {

    int countByScreeningAndReservation_Member(Screening screening, Member member);

    @Query("SELECT sr.seat FROM SeatReservation sr WHERE sr.screening = :screening")
    List<Seat> findReservedSeatsByScreening(Screening screening);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    boolean existsByScreeningAndSeat(Screening screening, Seat seat);

}
