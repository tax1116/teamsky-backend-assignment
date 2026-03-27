# 학습 플랫폼 - 단원별 문제 풀이 API

학습 플랫폼의 핵심 기능인 단원별 문제 풀이와 풀이 이력 조회 API를 구현한 과제입니다.

객체지향적 설계와 데이터 처리 성능을 고려하여, 단순 CRUD가 아닌 다음 세 가지에 목표를 두었습니다.

- 사용자가 단원별로 문제를 하나씩 풀 수 있는 학습 흐름 제공
- 정답 제출과 풀이 이력 조회를 안정적으로 처리하는 도메인 모델링
- 정답률과 랜덤 조회에서 조회 성능을 고려한 현실적인 설계

## 실행 방법

### Docker Compose (앱 + DB 한 번에 구동)

```bash
cd docker && docker compose up
```

- MySQL(`3306`)과 애플리케이션(`8080`)이 함께 실행됩니다.
- 최초 실행 시 `docker/init.sql`로 스키마와 샘플 데이터가 적재됩니다.
- 별도의 사전 빌드 없이 `docker compose up`만으로 실행할 수 있습니다.

```bash
cd docker && docker compose up -d    # 백그라운드 실행
cd docker && docker compose down     # 종료
cd docker && docker compose down -v  # 종료 + 볼륨 제거 (스키마 초기화)
```

### 로컬 개발

```bash
./gradlew :study:boot:bootRun
```

- 로컬 실행 전 MySQL이 `localhost:3306/teamsky`에서 실행 중이어야 합니다.
- 기본 계정 정보는 `teamsky / teamsky`입니다.

### API 문서

Swagger UI: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

### 빌드 및 테스트

```bash
./gradlew build              # 전체 모듈 빌드 및 테스트
./gradlew check              # 정적 검증 및 테스트 실행
./gradlew test               # 전체 테스트 실행
./gradlew :study:domain:test # 특정 모듈 테스트 실행
```

- Testcontainers 기반 통합 테스트가 포함되어 있어 Docker가 실행 중이어야 합니다.
- 도메인 채점 규칙 단위 테스트, API 통합 테스트(해피/실패 케이스), 동시 제출 시나리오 테스트를 포함합니다.
- 정적 검증(lint)도 함께 구성되어 있습니다.

## 기술 스택

- **Language:** Java 21
- **Framework:** Spring Boot 4.0.3
- **ORM:** JPA (Hibernate)
- **Database:** MySQL
- **Build:** Gradle (Kotlin DSL) 멀티모듈
- **Test:** JUnit 5, TestContainers (MySQL)

## 모듈 구조

```
study/
├── boot              # Spring Boot 애플리케이션 진입점
├── api               # REST 컨트롤러, 요청/응답 DTO, Validation
├── app               # 애플리케이션 서비스 (유스케이스, 트랜잭션 경계)
├── domain            # 순수 POJO 도메인 모델, 저장소 인터페이스 (Spring/JPA 의존성 없음)
├── infra/
│   ├── persistence   # JPA 엔티티, 리포지토리 구현체
│   └── cache         # 정답률 인메모리 집계 및 스킵 캐시
```

## 도메인 모델

| 도메인 | 설명 |
|---|---|
| Chapter | 단원 |
| Problem | 문제 (객관식/주관식 구분, 해설 포함, 채점 로직 위치) |
| Choice | 객관식 선택지 |
| Answer | 문제의 정답 (복수 정답 가능) |
| Submission | 유저의 풀이 기록 (제출 답안, 정답/부분정답/오답) |
| User | 사용자 |

- 도메인 모델은 Spring/JPA 어노테이션 없이 순수 Java record로 구성했습니다.
- 모든 ID는 Typed Value Object(ProblemId, UserId 등)로 감싸 타입 혼동을 방지했습니다.

## 핵심 설계 판단

### 랜덤 문제 조회

- 미풀이 문제는 `submission` 테이블 기준 NOT EXISTS로 판별합니다. `(userId, problemId)` 복합 인덱스를 활용합니다.
- DB `ORDER BY RAND()`는 풀스캔 이슈로 사용하지 않았습니다. 미풀이 문제 ID 목록을 조회한 뒤 애플리케이션에서 랜덤 선택합니다.
- skip 상태는 Caffeine 로컬 캐시에 `userId:chapterId → skippedProblemId` 형태로 관리하며, 다음 1회 조회에만 적용 후 즉시 제거됩니다.

### 답안 제출과 채점

- 채점 규칙은 `domain`의 `Problem` 객체에 두어 도메인 규칙을 한 곳에 모았습니다.
- 객관식은 1-base 선택지 번호, 주관식은 trim 후 exact match(복수 정답 허용)로 비교합니다.
- 부분 정답은 정답을 1개 이상 포함했지만 전체 정답 집합과 같지 않은 경우로 처리합니다.
- 같은 유저가 같은 문제를 중복 제출하면 409 Conflict로 응답합니다.

### 정답률 집계

- 정답률은 원본 제출 데이터(`submission`)로부터 계산되는 파생 집계 데이터로 보았습니다.
- 제출 성공 자체가 더 중요하다고 판단하여, 집계 정합성을 위해 제출 트랜잭션에 락/재시도를 강하게 묶지 않았습니다.
- 조회 성능을 위해 문제별 정답률은 인메모리 캐시(ConcurrentHashMap + AtomicInteger)에 보관하고, `@Scheduled` 스케줄러가 DB 집계값으로 주기적으로 보정합니다.
- 이 선택은 강한 일관성보다 조회 성능과 쓰기 경량화를 우선한 trade-off입니다.
- 부분 정답은 정답률 계산 시 오답으로 처리합니다. 30명 이상 풀었을 때만 정답률을 제공하며, 미만이면 null을 반환합니다.
- 멀티 인스턴스 환경은 이번 과제 범위에서 직접 고려하지 않았습니다. 해당 환경이 필요하면 글로벌 캐시(Redis) 또는 이벤트 전파 구조로 확장할 수 있으며, 스케줄러 중복 실행 방지는 ShedLock 등 분산 락으로 대응 가능합니다.

### 예외 처리

`ErrorCode` enum에 에러 코드, HTTP 상태, 메시지를 매핑하고 `BusinessException`으로 통일했습니다.

| 상황 | HTTP 상태 | 에러 코드 |
|---|---|---|
| 풀 수 있는 문제가 없음 | 404 | 4003 |
| 존재하지 않는 문제/이력 | 404 | 4001, 4002 |
| 이미 제출한 문제 재제출 | 409 | 4004 |
| 잘못된 answerType, 범위 초과 등 | 400 | 4005 |
