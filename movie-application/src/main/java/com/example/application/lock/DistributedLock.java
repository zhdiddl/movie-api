package com.example.application.lock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @distributedLock 커스텀 메서드를 선언하는 인터페이스
 * 현재는 AOP 대신 함수형 락을 적용하도록 리팩토링하여 사용하지 않는다.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {
    String key();
    long waitTime() default 2_000;   // 적당한 대기 시간을 고려해 잠금 시간의 2배 정도로 설정
    long leaseTime() default 1_000; // 지연을 고려해 트랜잭션 평균 실행 시간의 2배 정도로 설정
}

