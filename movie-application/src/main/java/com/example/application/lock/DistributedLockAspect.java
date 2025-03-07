package com.example.application.lock;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

/**
 * @distributedLock 붙인 메서드를 감지, Redisson 락을 실제 적용하는 클래스
 * 현재는 AOP 대신 함수형 락을 적용하도록 리팩토링하여 사용하지 않는다.
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAspect {

    private final RedissonClient redissonClient;

    @Around("@annotation(distributedLock)")
    public Object applyLock(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        String lockKey = distributedLock.key();
        long waitTime = distributedLock.waitTime();
        long leaseTime = distributedLock.leaseTime();

        RLock lock = redissonClient.getLock(lockKey);

        boolean acquired = lock.tryLock(waitTime, leaseTime, TimeUnit.MILLISECONDS);
        if (!acquired) {
            log.warn("[Distributed Lock] 락 획득 실패: {}", lockKey);
            throw new RuntimeException("Seat is already reserved by another transaction.");
        }

        try {
            log.info("[Distributed Lock] 락 획득 성공: {}", lockKey);
            return joinPoint.proceed();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.info("[Distributed Lock] 락 해제 완료: {}", lockKey);
            }
        }
    }

}
