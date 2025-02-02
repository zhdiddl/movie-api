package com.example.application.service;

import com.example.application.port.out.RateLimiterPort;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RedisRateLimiterService implements RateLimiterPort {

    private final RedisTemplate<String, String> redisTemplate;

    // Lua Script 실행을 위한 Redis Script 정의 (카운터 방식)
    // KEYS[1]: key, ARGV[1]: maxRequests, ARGV[2]: durationInSeconds
    private static final String RATE_LIMITER_LUA_SCRIPT =
            "local current = redis.call('GET', KEYS[1]) " + // 해당 키에 저장된 값을 가져옴
                    "if current and tonumber(current) > tonumber(ARGV[1]) then " + // 지금까지 요청 횟수 > 허용한 최대 요청 횟수
                    "   return 0 " + // 비교가 참이면 요청 차단
                    "else " +
                    "   redis.call('INCR', KEYS[1]) " + // INCR 명령어로 요청 횟수를 1 증가
                    "   redis.call('EXPIRE', KEYS[1], ARGV[2]) " + // ARGV[2]에 설정된 초가 지나가면 제한 해제하도록 TTL 설정
                    "   return 1 " + // 요청 허용
                    "end";

    @Override
    public boolean isAllowed(String key, int maxRequests, int durationInSeconds) {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>(RATE_LIMITER_LUA_SCRIPT, Long.class);
        Long result = redisTemplate.execute(script, Collections.singletonList(key),
                String.valueOf(maxRequests), String.valueOf(durationInSeconds));

        return result == 1; // 1이면 요청 허용, 0이면 차단
    }

}
