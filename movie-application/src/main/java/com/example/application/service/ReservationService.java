package com.example.application.service;

import com.example.application.dto.request.ReservationRequestDto;
import com.example.application.dto.response.ReservationResponseDto;
import com.example.application.port.in.MessageServicePort;
import com.example.application.port.in.ReservationServicePort;
import com.example.application.port.out.MemberRepositoryPort;
import com.example.application.port.out.ReservationRepositoryPort;
import com.example.application.port.out.ReservationValidationPort;
import com.example.application.port.out.ScreeningRepositoryPort;
import com.example.application.port.out.SeatRepositoryPort;
import com.example.application.port.out.SeatReservationRepositoryPort;
import com.example.domain.exception.CustomException;
import com.example.domain.exception.ErrorCode;
import com.example.domain.model.entity.Member;
import com.example.domain.model.entity.Reservation;
import com.example.domain.model.entity.Screening;
import com.example.domain.model.entity.Seat;
import com.example.domain.model.entity.SeatReservation;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReservationService implements ReservationServicePort {

    private final ReservationRepositoryPort reservationRepositoryPort;
    private final MemberRepositoryPort memberRepositoryPort;
    private final ScreeningRepositoryPort screeningRepositoryPort;
    private final SeatRepositoryPort seatRepositoryPort;
    private final SeatReservationRepositoryPort seatReservationRepositoryPort;
    private final ReservationValidationPort reservationValidationPort;
    private final MessageServicePort messageServicePort;

    @Override
    public ReservationResponseDto create(ReservationRequestDto request) {

        // 상영 정보 존재 여부 검증
        Screening screening = screeningRepositoryPort.findById(request.screeningId())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_SCREENING));

        // 회원 정보 존재 여부 검증
        Member member = memberRepositoryPort.findById(request.memberId())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_MEMBER));

        // 현재 사용자의 총 예약 좌석 수 검증
        int existingReservations = seatReservationRepositoryPort.countByScreeningAndReservationMember(screening, member);
        List<Seat> requestedSeats = seatRepositoryPort.findAllById(request.seatIds());
        reservationValidationPort.validateMaxSeatsPerScreening(existingReservations, requestedSeats.size());

        // 현재 상영 시간대의 예약 좌석 조회 후 요청한 좌석 예약 가능 여부 검증
        List<Seat> alreadyReservedSeats = seatReservationRepositoryPort.findReservedSeatsByScreening(screening);
        reservationValidationPort.validateSeatsAreAvailableForReservation(requestedSeats, alreadyReservedSeats);

        // 요청한 좌석이 연속 배치되었는지 검증
        reservationValidationPort.validateSeatsAreConsecutive(requestedSeats);

        // 예약 내역 생성
        Reservation reservation = Reservation.create(screening, member);
        reservationRepositoryPort.save(reservation);

        // 좌석 정보를 예약 내역에 저장
        for (Seat seat : requestedSeats) {
            SeatReservation seatReservation = SeatReservation.of(reservation, seat, screening);
            seatReservationRepositoryPort.save(seatReservation);
            reservation.getSeatReservations().add(seatReservation);
        }

        // 예약 완료 후 알림 메시지 전송
        sendReservationConfirmation(member, requestedSeats, screening);

        return ReservationResponseDto.fromEntity(reservation);
    }

    private void sendReservationConfirmation(Member member, List<Seat> requestedSeats, Screening screening) {
        messageServicePort.send(String.format("[예약 완료] 사용자명: %s, 좌석: %s, 영화: %s, 상영관: %s, 시간: %s",
                member.getName(),
                requestedSeats.stream().map(seat -> seat.getSeatNumber().toString()).collect(Collectors.joining(", ")),
                screening.getMovie().getTitle(), screening.getTheater().getName(), screening.getStartTime()));
    }

}
