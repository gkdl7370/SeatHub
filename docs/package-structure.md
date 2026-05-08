# 패키지 구조

## 기본 구조

```text
com.seathub
 ├── auth
 ├── user
 ├── product
 ├── schedule
 ├── seat
 ├── reservation
 ├── payment
 ├── admin
 ├── batch
 ├── common
 └── infra
```

---

## 패키지별 역할

| 패키지 | 역할 |
| --- | --- |
| `auth` | 로그인, JWT 발급, 인증 관련 처리 |
| `user` | 회원, 권한, 사용자 상태 관리 |
| `product` | 예약 상품 등록/조회 |
| `schedule` | 상품 회차 등록/조회 |
| `seat` | 좌석 등록/조회, 좌석 상태 관리 |
| `reservation` | 예약 생성, 취소, 상태 전이, 상태 이력 |
| `payment` | 결제 준비, 승인, 실패 처리, 외부 결제 Client 추상화 |
| `admin` | 관리자 검색 API, 운영자 전용 기능 |
| `batch` | 결제 대기 만료 배치 |
| `common` | 공통 응답, 예외, ErrorCode, traceId |
| `infra` | 외부 시스템 연동 구현, 설정 클래스 |

---

## 도메인 패키지 내부 구조

도메인 패키지는 아래 구조를 기본으로 사용합니다.

```text
reservation
 ├── controller
 ├── service
 ├── domain
 ├── repository
 ├── dto
 └── exception
```

| 하위 패키지 | 역할 |
| --- | --- |
| `controller` | HTTP 요청/응답 처리 |
| `service` | 유스케이스 흐름과 트랜잭션 경계 |
| `domain` | Entity, Enum, 도메인 메서드 |
| `repository` | JPA Repository, QueryDSL Repository |
| `dto` | Request/Response DTO |
| `exception` | 도메인 예외 |

---

## 구현 규칙

- Controller에는 비즈니스 로직을 넣지 않는다.
- Service는 유스케이스 흐름과 트랜잭션 경계를 책임진다.
- Entity에는 상태 변경처럼 도메인 규칙이 필요한 메서드를 둔다.
- DTO와 Entity를 직접 공유하지 않는다.
- 외부 결제 연동은 `PaymentClient` 인터페이스로 분리한다.
- 관리자 검색은 조회 전용 DTO Projection을 사용한다.
- 공통 예외 응답은 `common`에서 관리한다.

