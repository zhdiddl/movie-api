package com.example.infrastructure.persistence;

import com.example.application.port.out.ReservationRepositoryPort;
import com.example.domain.model.entity.Reservation;
import com.example.infrastructure.db.ReservationJpaRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ReservationJpaRepositoryAdapter implements ReservationRepositoryPort {

    private final ReservationJpaRepository reservationJpaRepository;

    @Override
    public void save(Reservation reservation) {
        reservationJpaRepository.save(reservation);
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return reservationJpaRepository.findById(id);
    }
}
