# 10일차 작업 기록

## 오늘의 목표

2주차 수요일에는 화요일에 만든 Product 등록 기능에 이어 사용자 상품 조회 API를 구현했습니다.

상품 조회는 사용자가 예약할 대상을 선택하기 위한 첫 단계입니다.

## 구현한 기능

### 상품 목록 조회

```http
GET /api/v1/products
```

등록된 상품 목록을 조회합니다.  
현재는 최신 등록 상품이 먼저 보이도록 `id` 내림차순으로 정렬합니다.

### 상품 상세 조회

```http
GET /api/v1/products/{productId}
```

상품 ID로 단건 상품 정보를 조회합니다.

상품이 없으면 `PRODUCT_NOT_FOUND` 예외를 반환합니다.

## 추가한 파일

```text
src/main/java/com/seathub/product/controller/ProductController.java
src/test/java/com/seathub/product/controller/ProductControllerTest.java
```

## 수정한 파일

```text
src/main/java/com/seathub/product/service/ProductService.java
src/main/java/com/seathub/common/api/ErrorCode.java
src/main/java/com/seathub/config/SecurityConfig.java
src/test/java/com/seathub/product/service/ProductServiceTest.java
docs/02_설계/05_API명세.md
```

## 설계 판단

### 조회 API를 공개한 이유

상품 목록과 상세 조회는 사용자가 예약 가능한 상품을 탐색하는 기능입니다.  
로그인 전에도 상품을 볼 수 있어야 하므로 인증 없이 접근 가능하도록 했습니다.

### 관리자 API와 분리한 이유

상품 등록은 데이터를 변경하는 운영 기능이므로 `/api/v1/admin/products`로 분리했습니다.  
상품 조회는 사용자 기능이므로 `/api/v1/products`로 분리했습니다.

### PRODUCT_NOT_FOUND를 추가한 이유

없는 상품을 조회했을 때 generic한 `RESOURCE_NOT_FOUND`보다 `PRODUCT_NOT_FOUND`가 더 명확합니다.

클라이언트와 테스트 코드 모두 실패 이유를 더 정확히 알 수 있습니다.

## 추가한 테스트

- 상품 목록 조회 Service 테스트
- 상품 상세 조회 Service 테스트
- 없는 상품 조회 실패 Service 테스트
- 상품 목록 조회 Controller 테스트
- 상품 상세 조회 Controller 테스트
- 없는 상품 조회 404 응답 테스트

## 다음 작업

다음 작업에서는 회차 도메인을 구현합니다.

예상 순서:

```text
Schedule Entity
ScheduleRepository
회차 등록 Request DTO
회차 응답 DTO
상품에 회차 등록 API
상품별 회차 목록 조회 API
```

