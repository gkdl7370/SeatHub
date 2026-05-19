# 9일차 작업 기록

## 오늘의 목표

2주차 화요일에는 상품 도메인의 첫 기능인 관리자 상품 등록 API를 구현했습니다.

상품은 예약 기능의 시작점입니다. 사용자가 예약하려면 먼저 관리자가 예약 가능한 상품을 등록해야 합니다.

## 구현한 기능

### Product Entity

`products` 테이블에 저장될 상품 Entity를 추가했습니다.

주요 필드는 다음과 같습니다.

```text
id
name
description
status
createdAt
updatedAt
```

상품은 생성 시 기본 상태를 `ACTIVE`로 가집니다.

### ProductStatus

상품 상태는 문자열이 아니라 enum으로 관리합니다.

```text
ACTIVE
INACTIVE
```

이를 통해 잘못된 상태 문자열이 들어가는 것을 줄이고, 상품 노출 여부를 명확히 표현할 수 있습니다.

### 관리자 상품 등록 API

관리자 상품 등록 API를 추가했습니다.

```http
POST /api/v1/admin/products
```

요청 예시:

```json
{
  "name": "뮤지컬 A",
  "description": "주말 공연 상품"
}
```

응답 예시:

```json
{
  "success": true,
  "data": {
    "id": 1,
    "name": "뮤지컬 A",
    "description": "주말 공연 상품",
    "status": "ACTIVE"
  },
  "traceId": "request-trace-id"
}
```

## 추가한 파일

```text
src/main/java/com/seathub/product/domain/Product.java
src/main/java/com/seathub/product/domain/ProductStatus.java
src/main/java/com/seathub/product/repository/ProductRepository.java
src/main/java/com/seathub/product/dto/CreateProductRequest.java
src/main/java/com/seathub/product/dto/ProductResponse.java
src/main/java/com/seathub/product/service/ProductService.java
src/main/java/com/seathub/product/controller/AdminProductController.java
src/main/resources/db/migration/V2__create_products.sql
```

## 추가한 테스트

```text
ProductServiceTest
AdminProductControllerTest
```

테스트에서는 다음을 확인했습니다.

- 상품 생성 시 ID가 발급되는가
- 상품명이 요청값과 같은가
- 설명이 요청값과 같은가
- 기본 상태가 ACTIVE인가
- HTTP API가 201 Created와 공통 응답 형식으로 응답하는가

## 설계 판단

### Entity와 DTO를 분리한 이유

요청에서는 `name`, `description`만 받습니다.  
`id`, `status`, `createdAt`, `updatedAt`은 서버가 정해야 하는 값이므로 요청 DTO와 Entity를 분리했습니다.

### 관리자 API로 둔 이유

상품 등록은 일반 사용자가 수행하는 기능이 아니라 운영자가 예약 가능한 상품을 준비하는 기능입니다.  
따라서 `/api/v1/admin/products` 경로로 분리했습니다.

### 상태를 enum으로 둔 이유

상품 상태는 정해진 값만 허용되어야 합니다.  
문자열로 직접 관리하면 오타나 잘못된 상태값이 들어갈 수 있으므로 enum으로 제한했습니다.

## 다음 작업

다음 작업에서는 상품 조회 API를 추가합니다.

예상 작업:

```text
상품 목록 조회
상품 단건 조회
상품이 없을 때 예외 처리
ProductService 테스트 추가
API 명세 문서 갱신
```

