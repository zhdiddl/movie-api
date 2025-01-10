package com.example.moviedomain.valueObject;

import static java.util.Objects.hash;

import com.example.moviecommon.exception.CustomException;
import com.example.moviecommon.exception.ErrorCode;
import jakarta.persistence.Embeddable;

@Embeddable
public class SeatNumber {

    private String seatRow;
    private int seatColumn;

    protected SeatNumber() {}

    private SeatNumber(String seatRow, int seatColumn) {
        this.seatRow = seatRow;
        this.seatColumn = seatColumn;
    }

    public static SeatNumber of(String row, int column) {
        if (!row.matches("[A-E]") || column < 1 || column > 5) {
            throw new CustomException(ErrorCode.INVALID_SEAT_NUMBER);
        }
        return new SeatNumber(row, column);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SeatNumber that)) return false;
        return seatColumn == that.seatColumn && seatRow.equals(that.seatRow); // 좌석 번호가 같은 경우 동일한 객체로 취급
    }

    @Override
    public int hashCode() {
        return hash(seatRow, seatColumn);
    }

    @Override
    public String toString() {
        return seatRow + seatColumn;
    }

}
