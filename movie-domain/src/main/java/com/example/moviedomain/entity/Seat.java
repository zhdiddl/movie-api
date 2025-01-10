package com.example.moviedomain.entity;

import com.example.moviecommon.exception.CustomException;
import com.example.moviecommon.exception.ErrorCode;
import com.example.moviedomain.base.AuditingFields;
import com.example.moviedomain.valueObject.SeatNumber;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Getter
@Entity
public class Seat extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Theater theater;

    @Embedded
    private SeatNumber seatNumber;


    protected Seat() {}

    private Seat(Theater theater, SeatNumber seatNumber) {
        this.theater = theater;
        this.seatNumber = seatNumber;
    }

    public static Seat of(Theater theater, SeatNumber seatNumber) {
        validate(theater, seatNumber);
        return new Seat(theater, seatNumber);
    }

    private static void validate(Theater theater, SeatNumber seatNumber) {
        if (theater == null) {
            throw new CustomException(ErrorCode.INVALID_THEATER);
        }
        if (seatNumber == null) {
            throw new CustomException(ErrorCode.INVALID_SEAT_NUMBER);
        }
    }

}
