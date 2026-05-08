# SeatHub API 명세 초안

## 공통 규칙

### Base URL

```text
/api/v1
```

### 공통 응답 형식

```json
{
  "success": true,
  "data": {},
  "traceId": "request-trace-id"
}
```

### 공통 에러 응답 형식

```json
{
  "success": false,
  "code": "RESERVATION_ALREADY_OCCUPIED",
  "message": "이미 예약된 좌석입니다.",
  "traceId": "request-trace-id"
}
```

---

## 인증 API

| Method | URL | 설명 | 인증 |
| --- | --- | --- | --- |
| POST | `/auth/signup` | 회원가입 | 불필요 |
| POST | `/auth/login` | 로그인 | 불필요 |
| POST | `/auth/reissue` | 토큰 재발급 | 필요 |
| POST | `/auth/logout` | 로그아웃 | 필요 |

---

## 상품 API

| Method | URL | 설명 | 인증 |
| --- | --- | --- | --- |
| GET | `/products` | 상품 목록 조회 | 불필요 |
| GET | `/products/{productId}` | 상품 상세 조회 | 불필요 |
| GET | `/products/{productId}/schedules` | 상품 회차 목록 조회 | 불필요 |
| GET | `/schedules/{scheduleId}/seats` | 회차 좌석 목록 조회 | 불필요 |

---

## 예약 API

| Method | URL | 설명 | 인증 |
| --- | --- | --- | --- |
| POST | `/reservations` | 예약 생성 | 필요 |
| GET | `/reservations/{reservationId}` | 예약 상세 조회 | 필요 |
| GET | `/me/reservations` | 내 예약 목록 조회 | 필요 |
| POST | `/reservations/{reservationId}/cancel` | 예약 취소 | 필요 |

---

## 결제 API

| Method | URL | 설명 | 인증 |
| --- | --- | --- | --- |
| POST | `/payments/ready` | 결제 준비 | 필요 |
| POST | `/payments/confirm` | 결제 승인 | 필요 |
| POST | `/payments/fail` | 결제 실패 처리 | 필요 |

---

## 관리자 API

| Method | URL | 설명 | 인증 |
| --- | --- | --- | --- |
| POST | `/admin/products` | 상품 등록 | 관리자 |
| POST | `/admin/products/{productId}/schedules` | 회차 등록 | 관리자 |
| POST | `/admin/schedules/{scheduleId}/seats` | 좌석 등록 | 관리자 |
| GET | `/admin/reservations` | 예약 검색 | 관리자 |
| GET | `/admin/payments` | 결제 검색 | 관리자 |

---

## 예약 생성 요청 예시

```json
{
  "scheduleId": 1,
  "seatId": 10
}
```

## 예약 생성 응답 예시

```json
{
  "success": true,
  "data": {
    "reservationId": 100,
    "status": "PAYMENT_PENDING",
    "expiresAt": "2026-05-08T18:30:00"
  },
  "traceId": "request-trace-id"
}
```

---

## 관리자 예약 검색 조건

| 파라미터 | 설명 |
| --- | --- |
| `status` | 예약 상태 |
| `productName` | 상품명 |
| `userEmail` | 예약자 이메일 |
| `from` | 예약 시작일 |
| `to` | 예약 종료일 |
| `page` | 페이지 번호 |
| `size` | 페이지 크기 |

