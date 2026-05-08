# Day 1 작업 기록

날짜: 2026-05-08

## 목표

SeatHub 프로젝트의 방향을 정하고, 구현 전에 요구사항과 API, 상태 흐름, 아키텍처 초안을 만든다.

## 완료한 작업

- [x] 로컬 `SeatHub` 폴더 생성
- [x] 로컬 Git 저장소 초기화
- [x] `.gitignore` 작성
- [x] README 초안 작성
- [x] 프로젝트 목표 작성
- [x] 요구사항 30개 작성
- [x] 주요 API 목록 작성
- [x] 예약 상태 흐름 작성
- [x] 아키텍처 초안 작성
- [x] 기존 포트폴리오 경험 재적용 방향 정리
- [x] 첫 커밋 생성

## 아직 남은 작업

- [ ] GitHub 원격 repo 생성
- [ ] ERD 초안 작성
- [ ] 기술 스택 최종 확정
- [ ] 패키지 구조 최종 확정
- [ ] Docker Compose 초안 작성

## 오늘 만든 파일

- `README.md`
- `docs/requirements.md`
- `docs/api-spec.md`
- `docs/state-flow.md`
- `docs/architecture.md`
- `docs/day-1-log.md`

## 첫 커밋

```text
docs: define SeatHub day 1 planning
```

## 다음 작업 후보

내일은 Day 2 작업으로 넘어갑니다.

1. ERD 초안 작성
2. 예약 상태 흐름 보완
3. 기술 스택 확정
4. 패키지 구조 작성
5. Docker Compose 초안 작성

## 방향 보완

SeatHub는 완전히 별개의 토이프로젝트로 만들기보다 기존 GitHub 프로젝트에서 다룬 주제를 하나의 예약·결제 도메인에 다시 적용하는 방향으로 진행한다.

- `One-Core-Architecture`: 도메인 경계와 외부 규격 분리
- `Data-Flux`: 배치 처리와 성능 측정
- `MassFlux-Gateway`: 고처리량 요청 처리와 병목 분석
- `kafka-practice`: 이벤트 기반 처리, 재시도, DLQ 설계
- `SimpleIoT.Gateway`: 운영 추적성과 상태 관리 관점
