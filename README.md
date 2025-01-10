## [본 과정] 이커머스 핵심 프로세스 구현
[단기 스킬업 Redis 교육 과정](https://hh-skillup.oopy.io/) 을 통해 상품 조회 및 주문 과정을 구현하며 현업에서 발생하는 문제를 Redis의 핵심 기술을 통해 해결합니다.
> Indexing, Caching을 통한 성능 개선 / 단계별 락 구현을 통한 동시성 이슈 해결 (낙관적/비관적 락, 분산락 등)

## 모듈 구성
```
/redis_1st
├── movie-api               # ✅ 외부 진입점 (API)
│   ├── MovieApiApplication # 애플리케이션 진입점, 모든 모듈 스캔
│   ├── controller          # API 컨트롤러
│   └── resources
│       └── application.yml # 애플리케이션 환경 설정
│
├── movie-application       # ✅ 비즈니스 로직 (Service, Port)
│   ├── service             # 비즈니스 서비스
│   ├── port                
│   │   └── in              # 서비스 포트
│   │   └── out             # 레포지토리 포트
│   └── dto                 # 클라이언트 반환을 위한 dto
│
├── movie-adapter           # ✅ 인프라스트럭처 어댑터
│   └── db                  # DB 레포지토리 및 어댑터 (JPA, Redis)
│       └── config          # DB 환경 설정
│   └── resources
│       └── application-dev.yml  # DB 연결 정보 (개발용)
│       └── application-prod.yml # DB 연결 정보 (운영용)
│
├── movie-domain            # ✅ 순수 도메인 엔티티
│   └── entity              # 엔티티 클래스 (Movie, Screening 등)
│   └── valueObject         # VO (SeatNumber, ContentRating 등 불변 객체)
│   └── base                # 공통 메타데이터 및 상속 클래스 (AuditingFields)
│
├── movie-common            # ✅ 공통 유틸리티
│   └── exception           # 공통 예외 처리 (CustomException)
│   └── utils               # 유틸리티 클래스
```
### 모듈 역할
- movie-api → Input Adapter & User Interface
- movie-application → Business Logic (Use Case, Port)
- movie-adapter → Output Adapter
- movie-domain → Entity, Value Object

## 아키텍처
- `헥사고날 아키텍처` 기반으로 Port, Adapter를 활용
- 핵심 비즈니스 로직이 외부에 의존하지 않도록 설계
- 포트를 통해 데이터베이스와 같은 외부 시스템과 상호작용
- 어댑터를 통해 포트를 세부 구현

## 테이블 디자인
- 테이블 관계
```aiignore
Movie 영화 → Screening 상영일시 (1:N)
Screening 상영일시 → Seat 좌석 (1:N)
Screening 상영일시 → Theater 상영관 (N:1)
```
- 모든 테이블은 createdBy, createdAt, modifiedBy, modifiedAt 포함