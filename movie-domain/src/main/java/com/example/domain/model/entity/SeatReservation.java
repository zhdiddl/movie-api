package com.example.domain.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Getter
@Entity
public class SeatReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Seat seat;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Screening screening;

    protected SeatReservation() {}

    private SeatReservation(Reservation reservation, Seat seat, Screening screening) {
        this.reservation = reservation;
        this.seat = seat;
        this.screening = screening;
    }

    public static SeatReservation of(Reservation reservation, Seat seat, Screening screening) {
        return new SeatReservation(reservation, seat, screening);
    }

}
