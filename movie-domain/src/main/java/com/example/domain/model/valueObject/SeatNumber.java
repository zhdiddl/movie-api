package com.example.domain.model.valueObject;

import static java.util.Objects.hash;

import jakarta.persistence.Embeddable;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;

@Getter
@Embeddable
public class SeatNumber {

    private static final Set<Character> VALID_SEAT_ROW = Set.of('A', 'B', 'C', 'D', 'E');
    private static final int MIN_SEAT_NUMBER = 1;
    private static final int MAX_SEAT_NUMBER = 5;

    private Character seatRow;
    private int seatColumn;

    protected SeatNumber() {}

    private SeatNumber(Character seatRow, int seatColumn) {
        assert isValidSeatRow(seatRow) : "[ASSERTION FAILED] Invalid seat row: " + seatRow;
        assert isValidSeatColumn(seatColumn) : "[ASSERTION FAILED] Invalid seat column: " + seatColumn;
        this.seatRow = seatRow;
        this.seatColumn = seatColumn;
    }

    public static SeatNumber of(Character row, int column) {
        return new SeatNumber(row, column);
    }

    private static boolean isValidSeatRow(Character seatRow) {
        return seatRow != null && VALID_SEAT_ROW.contains(seatRow);
    }

    private static boolean isValidSeatColumn(int seatColumn) {
        return seatColumn >= MIN_SEAT_NUMBER && seatColumn <= MAX_SEAT_NUMBER;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // 참조가 동일하면 true
        if (!(o instanceof SeatNumber that)) return false; // SeatNumber 인스턴스 아니면 false
        return this.seatColumn == that.seatColumn && this.seatRow.equals(that.seatRow); // 좌석 번호가 같은 경우 동일한 객체로 취급
    }

    @Override
    public int hashCode() {
        return hash(seatRow, seatColumn);
    }

    @Override
    public String toString() {
        return seatRow.toString() + seatColumn;
    }

}
