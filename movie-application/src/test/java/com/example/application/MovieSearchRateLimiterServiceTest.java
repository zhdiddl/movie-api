package com.example.application;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.application.service.MovieSearchRateLimiterService;
import java.util.concurrent.TimeUnit;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MovieSearchRateLimiterServiceTest {

    private MovieSearchRateLimiterService movieSearchRateLimiterService;
    private String testIp;

    @BeforeEach
    void setUp() {
        movieSearchRateLimiterService = new MovieSearchRateLimiterService();
        testIp = "192.168.1.1"; // 임의의 IP
    }

    @DisplayName("특정 IP의 첫 번째 요청은 항상 허용되어야 한다.")
    @Test
    void givenFirstRequest_whenCheckingIfAllowed_thenReturnTrue() {
        // When
        boolean allowed = movieSearchRateLimiterService.isAllowed(testIp);

        // Then
        assertTrue(allowed, "첫 번째 요청은 항상 허용되어야 합니다.");
    }

    @DisplayName("동일한 IP가 1분 안에 50회 요청한 경우, 초과한 요청은 실패한다.")
    @Test
    void givenRateLimit_whenExceedRequests_thenBlockRequests() throws InterruptedException {
        // Given: 50개 요청 수행
        for (int i = 0; i < 50; i++) {
            Thread.sleep(1200); // 초당 0.8개 요청을 처리할 수 있기 때문에 50개가 성공할 수 있는 간격으로 요청
            assertTrue(movieSearchRateLimiterService.isAllowed(testIp), "50개까지 요청이 허용되어야 합니다.");
        }

        // When: 51번째 요청 수행 (즉시 요청)
        boolean allowedImmediately = movieSearchRateLimiterService.isAllowed(testIp);

        // Then: 51번째 요청은 차단되어야 한다.
        assertFalse(allowedImmediately, "51번째 요청은 즉시 차단되어야 합니다.");
    }

    @DisplayName("다른 IP는 별도의 RateLimiter를 갖는다.")
    @Test
    void givenDifferentIpRequest_whenCheckingIfAllowed_thenSeparateRateLimiterProvided() throws InterruptedException {
        // Given: 첫 번째 IP가 1분 이내에 50회 요청
        for (int i = 0; i < 50; i++) {
            Thread.sleep(1200); // 초당 0.8개 요청을 처리할 수 있기 때문에 50개가 성공할 수 있는 간격으로 요청
            assertTrue(movieSearchRateLimiterService.isAllowed(testIp), "50개까지 요청이 허용되어야 합니다.");
        }

        // When: 두 번째 IP가 1분 이내 첫 요청 전송
        String anotherIp = "192.168.1.2";
        boolean allowed = movieSearchRateLimiterService.isAllowed(anotherIp);

        // Then
        assertTrue(allowed, "두 번째 IP는 별도의 Rate Limiter를 가지므로 요청이 성공해야 합니다.");
    }

    @Test
    @DisplayName("50개 요청 후 51번째 요청이 차단되며, 일정 시간 동안 요청이 허용되지 않아야 한다.")
    void givenRateLimit_whenExceedRequests_thenBlockForAWhile() throws InterruptedException {
        // Given: 50개 요청 수행
        for (int i = 0; i < 50; i++) {
            Thread.sleep(1200); // 초당 0.8개 요청을 처리할 수 있기 때문에 50개가 성공할 수 있는 간격으로 요청
            assertTrue(movieSearchRateLimiterService.isAllowed(testIp), "50개까지 요청이 허용되어야 합니다.");
        }

        // When: 토큰이 충전될 시간을 주지 않고 바로 51번째 요청
        boolean allowedImmediately = movieSearchRateLimiterService.isAllowed(testIp);

        // Then: 51번째 요청은 차단되어야 함
        assertFalse(allowedImmediately, "51번째 요청은 즉시 차단되어야 합니다.");

        // 일정 시간 동안 51번째 요청이 계속 차단되는지 확인
        Awaitility.await()
                .atMost(1, TimeUnit.SECONDS) // 최대 1초 동안
                .pollInterval(100, TimeUnit.MILLISECONDS) // 100ms마다 요청이 차단되는지 확인
                .until(() -> !movieSearchRateLimiterService.isAllowed(testIp));

        // 2초 후에도 여전히 차단 상태여야 함
        assertFalse(movieSearchRateLimiterService.isAllowed(testIp), "2초 후에도 요청이 차단 상태여야 합니다.");
    }

}
