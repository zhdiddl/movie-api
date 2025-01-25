package com.example.application.service;

import com.example.application.dto.request.ReservationRequestDto;
import com.example.application.dto.response.ReservationResponseDto;
import com.example.application.port.in.ReservationServicePort;
import com.example.application.port.out.MemberRepositoryPort;
import com.example.application.port.out.ReservationRepositoryPort;
import com.example.application.port.out.ReservationValidationPort;
import com.example.application.port.out.ScreeningRepositoryPort;
import com.example.application.port.out.SeatRepositoryPort;
import com.example.application.port.out.SeatReservationRepositoryPort;
import com.example.domain.model.entity.Member;
import com.example.domain.model.entity.Reservation;
import com.example.domain.model.entity.Screening;
import com.example.domain.model.entity.Seat;
import com.example.domain.model.entity.SeatReservation;
import java.util.List;
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

    @Override
    public ReservationResponseDto create(ReservationRequestDto request) {

        // 상영 정보 조회 및 검증
        Screening screening = screeningRepositoryPort.findById(request.screeningId())
                .orElse(null);
        reservationValidationPort.validateScreening(screening);

        // 회원 정보 조회 및 검증
        Member member = memberRepositoryPort.findById(request.memberId())
                .orElse(null);
        reservationValidationPort.validateMember(member);

        //  요청한 좌석 조회 및 검증
        List<Long> requestedSeatIds = request.seatIds();
        List<Seat> requestedSeats = seatRepositoryPort.findAllById(requestedSeatIds);
        reservationValidationPort.validateRequestedSeatsExisted(requestedSeatIds, requestedSeats);

        // 현재 사용자의 기존 예약 좌석 개수 조회 및 검증
        int existingReservations = seatReservationRepositoryPort.countByScreeningAndReservationMember(screening, member);
        reservationValidationPort.validateMaxSeatsPerScreening(existingReservations, requestedSeats.size());

        // 현재 상영 시간대에서 요청한 좌석의 예약 여부 조회 및 검증
        List<Seat> alreadyReservedSeats = seatReservationRepositoryPort.findReservedSeatsByScreening(screening);
        reservationValidationPort.validateSeatsAreAvailableForReservation(requestedSeats, alreadyReservedSeats);

        // 요청한 좌석이 연속적으로 배치되었는지 검증
        reservationValidationPort.validateSeatsAreConsecutive(requestedSeats);

        // 예약 내역 생성
        Reservation reservation = Reservation.create(screening, member);
        reservationRepositoryPort.save(reservation);

        // 좌석 정보를 예약 내역에 저장
        for (Seat seat : requestedSeats) {
            SeatReservation seatReservation = SeatReservation.of(reservation, seat, screening);
            seatReservationRepositoryPort.save(seatReservation);
        }

        return ReservationResponseDto.fromEntity(reservation);
    }

}
