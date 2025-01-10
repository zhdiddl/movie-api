package com.example.movieadapter.db;

import com.example.moviedomain.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatJpaRepository extends JpaRepository<Seat, Long> {
}
