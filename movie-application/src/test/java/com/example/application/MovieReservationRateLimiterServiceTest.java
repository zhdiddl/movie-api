package com.example.application;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.application.service.MovieReservationRateLimiterService;
import java.util.concurrent.TimeUnit;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MovieReservationRateLimiterServiceTest {

    private MovieReservationRateLimiterService rateLimiterService;
    private Long screeningId;

    @BeforeEach
    void setUp() {
        rateLimiterService = new MovieReservationRateLimiterService();
        screeningId = 1L; // 임의의 상영 ID
    }

    @DisplayName("첫 번째 요청은 항상 허용되어야 한다.")
    @Test
    void givenFirstRequest_whenCheckingIfAllowed_thenReturnTrue() {
        // When
        boolean result = rateLimiterService.isAllowed(screeningId);

        // Then
        assertTrue(result, "첫 번째 요청은 항상 허용되어야 합니다.");
    }

    @DisplayName("두 번째 요청은 5분 내에는 차단되어야 한다.")
    @Test
    void givenSecondRequestWithinFiveMinutes_whenCheckingIfAllowed_thenReturnFalse() {
        // Given: 첫 번째 요청으로 토큰 사용
        rateLimiterService.isAllowed(screeningId);

        // When: 두 번째 요청 5분 동안 시도
        Awaitility.await()
                .atMost(300, TimeUnit.SECONDS)
                .pollInterval(10, TimeUnit.SECONDS)
                .until(() -> !rateLimiterService.isAllowed(screeningId));

        // Then
        assertFalse(rateLimiterService.isAllowed(screeningId), "두 번째 요청은 5분 내에는 차단되어야 합니다.");
    }

    @DisplayName("5분 후에는 다시 요청이 허용되어야 한다.")
    @Test
    void givenRequestAfterFiveMinutes_whenCheckingIfAllowed_thenReturnTrue() throws InterruptedException {
        // Given: 첫 번째 요청으로 토큰 사용
        rateLimiterService.isAllowed(screeningId);

        // When: 5분 대기 설정
        Thread.sleep(300 * 1000L);

        // 두 번째 요청
        boolean result = rateLimiterService.isAllowed(screeningId);

        // Then
        assertTrue(result, "5분 후에는 다시 요청이 허용되어야 합니다.");
    }

    @DisplayName("다른 상영 ID는 별도의 RateLimiter를 가져야 한다.")
    @Test
    void givenDifferentScreeningIds_whenCheckingIfAllowed_thenSeparateRateLimiters() {
        // Given: 첫 번째 상영 ID는 요청을 사용함
        rateLimiterService.isAllowed(screeningId);

        // When: 다른 상영 ID로 요청
        Long anotherScreeningId = 2L;
        boolean result = rateLimiterService.isAllowed(anotherScreeningId);

        // Then
        assertTrue(result, "다른 상영 ID는 별도의 RateLimiter를 가져야 합니다.");
    }

}
