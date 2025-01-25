package com.example.domain.validation;

import com.example.domain.exception.CustomException;
import com.example.domain.exception.ErrorCode;
import com.example.domain.model.entity.Seat;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ReservationValidation {

    private static final int MAX_SEATS_PER_SCREENING = 5;

    public void validateRequestedSeatsExisted(List<Long> requestedSeatIds, List<Seat> foundSeats) {
        if (Objects.isNull(requestedSeatIds) || requestedSeatIds.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_REQUEST, "좌석 ID 리스트가 null이거나 비어 있습니다.");
        }
        if (Objects.isNull(foundSeats) || foundSeats.isEmpty()) {
            throw new CustomException(ErrorCode.SEAT_NOT_FOUND);
        }
        if (foundSeats.size() != requestedSeatIds.size()) {
            throw new CustomException(ErrorCode.SEAT_NOT_FOUND);
        }
    }

    public void validateMaxSeatsPerScreening(int existingReservations, int newSeatCount) {
        if (existingReservations + newSeatCount > MAX_SEATS_PER_SCREENING) {
            throw new CustomException(ErrorCode.MAX_SEATS_EXCEEDED);
        }
    }

    public void validateSeatsAreAvailableForReservation(List<Seat> requestedSeats, List<Seat> alreadyReservedSeats) {
        if (Objects.isNull(requestedSeats) || requestedSeats.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_REQUEST, "요청된 좌석 리스트가 null이거나 비어 있습니다.");
        }
        if (Objects.isNull(alreadyReservedSeats)) {
            throw new CustomException(ErrorCode.INVALID_REQUEST, "예약된 좌석 리스트가 null입니다.");
        }
        for (Seat seat : requestedSeats) {
            if (alreadyReservedSeats.contains(seat)) {
                throw new CustomException(ErrorCode.SEAT_ALREADY_RESERVED);
            }
        }
    }

    public void validateSeatsAreConsecutive(List<Seat> seats) {
        if (Objects.isNull(seats) || seats.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_REQUEST, "요청된 좌석 리스트가 null이거나 비어 있습니다.");
        }

        Map<Character, List<Integer>> rowGroupedSeats = seats.stream()
                .collect(Collectors.groupingBy(
                        seat -> seat.getSeatNumber().getSeatRow(), // 좌석의 행으로 그룹화 (A, B, C, D, E)
                        Collectors.mapping(seat -> seat.getSeatNumber().getSeatColumn(), Collectors.toList()) // 좌석의 열을 리스트로 저장
                )); // rowGroupedSeats 예시: { 'A' -> [1, 2], 'B' -> [3] } (이 경우 size()는 2개)

        if (rowGroupedSeats.size() > 1) {
            throw new CustomException(ErrorCode.SEATS_NOT_CONSECUTIVE);
        }

        List<Integer> seatNumbers = rowGroupedSeats.values().iterator().next(); // rowGroupedSeats 첫 번째 리스트만 가져옴
        seatNumbers.sort(Integer::compareTo); // 오른차순 정렬

        for (int i = 0; i < seatNumbers.size() - 1; i++) {
            if (seatNumbers.get(i) + 1 != seatNumbers.get(i + 1)) { // 현재 좌석과 다음 좌석의 차이가 1인지 확인
                throw new CustomException(ErrorCode.SEATS_NOT_CONSECUTIVE);
            }
        }
    }

}
