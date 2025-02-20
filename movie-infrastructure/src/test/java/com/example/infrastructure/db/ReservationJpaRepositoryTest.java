package com.example.infrastructure.db;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.domain.model.entity.Member;
import com.example.domain.model.entity.Movie;
import com.example.domain.model.entity.Reservation;
import com.example.domain.model.entity.ReservedSeat;
import com.example.domain.model.entity.Screening;
import com.example.domain.model.entity.ScreeningSeat;
import com.example.domain.model.entity.Seat;
import com.example.domain.model.entity.Theater;
import com.example.domain.model.valueObject.ContentRating;
import com.example.domain.model.valueObject.Genre;
import com.example.domain.model.valueObject.SeatNumber;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;

@DisplayName("[JPA 테스트] 예약 리포지토리")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 실제 DB에서 테스트
@Import(ReservationJpaRepositoryTest.TestRedisConfig.class)
@DataJpaTest
class ReservationJpaRepositoryTest {

    @Autowired
    private ReservationJpaRepository sut;

    @PersistenceContext
    private EntityManager entityManager;

    private Reservation reservation;
    private Screening screening;
    private Member member;
    private List<ReservedSeat> reservedSeats;

    @BeforeEach
    void setUp() {
        // mock 객체 생성
        member = Member.of("Test User", "testuser@email.net");
        Movie movie = Movie.of("Test Movie", ContentRating.ADULT, LocalDate.of(2024, 12, 25),
                "http://test.com/image01", 66, Genre.COMEDY);
        Theater theater = Theater.of("Test Theater");
        screening = Screening.of(LocalTime.of(13, 15, 0), movie, theater);

        // 개별 엔티티 먼저 영속화
        entityManager.persist(member);
        entityManager.persist(movie);
        entityManager.persist(theater);
        entityManager.persist(screening);

        Seat seat1 = Seat.of(SeatNumber.of('C', 1), theater);
        Seat seat2 = Seat.of(SeatNumber.of('C', 2), theater);
        ScreeningSeat screeningSeat1 = ScreeningSeat.of(screening, seat1);
        ScreeningSeat screeningSeat2 = ScreeningSeat.of(screening, seat2);

        entityManager.persist(seat1);
        entityManager.persist(seat2);
        entityManager.persist(screeningSeat1);
        entityManager.persist(screeningSeat2);

        reservation = Reservation.of(screening, member);
        sut.save(reservation);

        reservedSeats = List.of(
                ReservedSeat.of(reservation, screeningSeat1),
                ReservedSeat.of(reservation, screeningSeat2)
        );
        reservation.addReservedSeats(reservedSeats);

        entityManager.flush();
        entityManager.clear();
    }

    @DisplayName("저장된 예약을 조회할 수 있어야 한다.")
    @Test
    void givenReservation_whenSaveAndFindById_thenReturnReservation() {
        // Given
        Optional<Reservation> foundReservation = sut.findById(reservation.getId());

        // When & Then
        assertThat(foundReservation).isPresent();
        assertThat(foundReservation.get().getMember().getName()).isEqualTo("Test User");
    }

    @DisplayName("특정 상영의 특정 회원 예약 수를 반환할 수 있어야 한다.")
    @Test
    void givenScreeningAndMember_whenCountReservations_thenReturnCount() {
        // Given & When
        int count = sut.countByScreeningAndMember(screening, member);

        // Then
        assertThat(count).isEqualTo(1);
    }

    @DisplayName("모든 예약을 삭제할 수 있어야 한다.")
    @Test
    void givenReservations_whenDeleteAll_thenAllReservationsAreDeleted() {
        // Given & When
        sut.deleteAll();
        long count = sut.countByScreeningAndMember(screening, member);

        // Then
        assertThat(count).isEqualTo(0);
    }


    @TestConfiguration
    static class TestRedisConfig {
        @Bean(name = "mockRedisTemplate")
        public RedisTemplate mockRedisTemplate() {
            return Mockito.mock(RedisTemplate.class);
        }
    }

}
