package com.example.application.service;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MovieReservationRateLimiterService {

    private static final int COOLDOWN_MINUTES = 5;
    private final Map<String, RateLimiter> movieRateLimiters = new ConcurrentHashMap<>();

    public boolean isAllowed(Long screeningId) {
        String key = "screening-" + screeningId;

        // 영화 상영 ID별로 RateLimiter 생성 (5분 동안 1번만 허용)
        RateLimiter rateLimiter = movieRateLimiters.computeIfAbsent(key, k ->
                RateLimiter.create(1.0 / (COOLDOWN_MINUTES * 60)) // 300초 (5분 * 60초) 동안 1회 허용
        );

        // 즉시 획득 가능하면 예약 허용, 아니면 차단
        return rateLimiter.tryAcquire();
    }

}
