package com.example.application.service;

import com.google.common.util.concurrent.RateLimiter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class MovieSearchRateLimiterService {

    private static final int MAX_REQUESTS_PER_MINUTE = 50;
    private final Map<String, RateLimiter> ipRateLimiters = new ConcurrentHashMap<>();

    public boolean isAllowed(String ip) {
        RateLimiter rateLimiter = ipRateLimiters.computeIfAbsent(ip, key ->
                RateLimiter.create(MAX_REQUESTS_PER_MINUTE / 60.0) // 초당 약 0.8개 요청 충전
        );

        // 즉시 획득 시 요청 허용, 아니면 차단
        return rateLimiter.tryAcquire();
    }

}
