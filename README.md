## [본 과정] 이커머스 핵심 프로세스 구현
[단기 스킬업 Redis 교육 과정](https://hh-skillup.oopy.io/) 을 통해 상품 조회 및 주문 과정을 구현하며 현업에서 발생하는 문제를 Redis의 핵심 기술을 통해 해결합니다.
> Indexing, Caching을 통한 성능 개선 / 단계별 락 구현을 통한 동시성 이슈 해결 (낙관적/비관적 락, 분산락 등)

## 모듈 구성
```
redis-1st
│
├── movie-domain                  ✅ 도메인 모듈 (핵심 비즈니스 로직)
│   └── src/main/java/com/example/domain
│       ├── model                 # 도메인 모델 (엔티티, 값 객체 등)
│       ├── converter             # 도메인 관련 유틸 (db 값 컨버터)
│       └── exception             # 도메인 예외 처리 (커스텀 익셉션, 에러 코드 등)
│
├── movie-application             ✅  애플리케이션 서비스 모듈
│   └── src/main/java/com/example/application
│       ├── port                  # 서비스/리포지토리 포트 (인터페이스)
│       ├── service               # 도메인 서비스 구현
│       ├── dto                   # 데이터 전송 객체 (DTO)
│       └── exception             # 애플리케이션 예외 처리 (핸들러)
│
├── movie-infrastructure          ✅  인프라스트럭처 모듈 + 애플리케이션 진입점
│   └── src/main/java/com/example/infrastructure
│       ├── web                   # 웹 관련 어댑터 (컨트롤러)
│       └── persistence           # DB 어댑터 
│       └── db                    # DB 구현체 
│            └── config           # DB 설정
└──
```
- `movie-infrastructure` 모듈의 `infrastructureApplication`로 Spring Boot 애플리케이션을 실행합니다.

### 모듈 역할
`헥사고날 아키텍처`를 기반으로 한 멀티모듈 구성

#### movie-domain
- `도메인` 모듈은 다른 모듈에 의존하지 않습니다.
- 외부 기술의 저수준 변경사항으로부터 도메인을 지키는 헥사고날 아키텍처 원칙을 지향합니다.
- 단, 아키텍처가 생산성을 저하시키지 않도록 JPA 엔티티를 예외적으로 허용했습니다.
- 도메인의 핵심 로직을 책임지는 엔티티, 값 객체, 예외, 변환기 등의 요소를 포함합니다.

#### movie-application 
- `애플리케이션` 모듈은 `도메인` 모듈에 의존합니다. 
- Inboud Port: 컨트롤러에서 DTO로 데이터를 주고 받을 때 호출할 서비스 포트를 제공합니다.
- Outbound Port: DB와 통신하기 위해 서비스 계층에서 호출할 리포지토리 포트를 정의합니다.

#### movie-infrastructure
- `인프라스트럭처` 모듈은 `애플리케이션` 모듈과 `도메인` 모듈에 의존합니다.
- 외부 시스템 및 DB와의 연결을 담당합니다.
- Persistence adapter: 저장소와 상호작용하기 위해 리포지토리 포트를 구현합니다.


## 테이블 디자인
- `Movie` (영화): 영화의 기본 정보 저장
- `Theater` (상영관): 상영관 정보를 관리
- `Screening` (상영 정보): movie_id, theater_id를 각각 외래키로 참조하며 영화와 상영관을 연결
- `Seat` (좌석 정보): theater_id를 외래키로 참조하여 각 상영관의 좌석 정보 저장
    - `SeatNumber`를 Embedded 타입으로 구성해 좌석 번호 관련 로직을 분리 
- `ContentRating` (상영 등급)을 Enum 으로 작성해 잘못된 값 입력을 방지
- 모든 엔티티는 `AuditingFields` 를 상속해 createdBy, createdAt, modifiedBy, modifiedAt 정보를 관리

####  테이블 관계 설정
```
Screening (N) <-> Movie (1)
Screening (N) -> Theater (1)
Seat (N) -> Theater (1)
```

#### 데이터 설정
- `v1.0__initial_schema.sql`로 DB 스키마를 정의합니다.
- `data.sql`로 애플리케이션 시작 시 DB에 초기 데이터를 삽입합니다.
