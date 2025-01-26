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
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    @Override
    public ReservationResponseDto create(ReservationRequestDto request) {
        Screening screening = getScreening(request.screeningId());
        Member member = getMember(request.memberId());

        List<Seat> requestedSeats = seatRepositoryPort.findAllById(request.seatIds());
        validateReservationConstraints(screening, member, requestedSeats);

        Reservation reservation = saveReservationAndSeats(screening, member, requestedSeats);
        sendReservationConfirmation(member, requestedSeats, screening);

        return ReservationResponseDto.fromEntity(reservation);
    }

    /** 상영 정보 조회 */
    private Screening getScreening(Long screeningId) {
        return screeningRepositoryPort.findById(screeningId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_SCREENING));
    }

    /** 회원 정보 조회 */
    private Member getMember(Long memberId) {
        return memberRepositoryPort.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_MEMBER));
    }

    /** 예약 전 비즈니스 로직 기반으로 요청 값 검증 */
    private void validateReservationConstraints(Screening screening, Member member, List<Seat> requestedSeats) {
        int existingReservations = seatReservationRepositoryPort.countByScreeningAndReservationMember(screening, member);
        reservationValidationPort.validateSeatsExist(requestedSeats.stream().map(Seat::getId).toList(), requestedSeats);
        reservationValidationPort.validateMaxSeatsPerScreening(existingReservations, requestedSeats.size());

        List<Seat> alreadyReservedSeats = seatReservationRepositoryPort.findReservedSeatsByScreening(screening);
        reservationValidationPort.validateSeatsAreConsecutive(requestedSeats);
        reservationValidationPort.validateSeatsAreAvailableForReservation(requestedSeats, alreadyReservedSeats);
    }

    /** 예약 및 좌석 저장 */
    private Reservation saveReservationAndSeats(Screening screening, Member member, List<Seat> requestedSeats) {
        Reservation reservation = Reservation.create(screening, member);
        reservationRepositoryPort.save(reservation);

        for (Seat seat : requestedSeats) {
            // 좌석에 pessimistic lock 적용하면서 예약된 좌석인지 확인
            boolean alreadyReserved = seatReservationRepositoryPort.existsByScreeningAndSeat(screening, seat);
            if (alreadyReserved) {
                throw new CustomException(ErrorCode.SEAT_ALREADY_RESERVED);
            }

            SeatReservation seatReservation = SeatReservation.of(reservation, seat, screening);
            seatReservationRepositoryPort.save(seatReservation);
            reservation.getSeatReservations().add(seatReservation); // 좌석 정보를 예약 내역에 저장
        }
        return reservation;
    }

    /** 예약 완료 후 알림 전송 */
    private void sendReservationConfirmation(Member member, List<Seat> requestedSeats, Screening screening) {
        messageServicePort.send(String.format("[예약 완료] 사용자명: %s, 좌석: %s, 영화: %s, 상영관: %s, 시간: %s",
                member.getName(),
                requestedSeats.stream().map(seat -> seat.getSeatNumber().toString()).collect(Collectors.joining(", ")),
                screening.getMovie().getTitle(), screening.getTheater().getName(), screening.getStartTime()));
    }

}
