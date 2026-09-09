# meter-api — Spring Boot 3 + JPA 검침 조회 API

검침 원본이 수천만 행일 때 조회 API를 어떻게 짜야 안 느려지는지 정리한 프로젝트입니다. 핵심은 두 가지.
OFFSET 대신 커서로 페이지를 넘기고, 월 집계는 원본이 아니라 요약 테이블에서 읽습니다.
앞선 mariadb-query-optimization에서 잡은 스키마 위에 얹었습니다.

---

## 기술 스택
- Java 17, Spring Boot 3.3, Spring Data JPA (Hibernate 6)
- MariaDB 10.11 (10.3+ 호환)
- Gradle

---

## 무엇을 다뤘나

### 1. 커서 기반 페이지네이션 (OFFSET 제거)
`OFFSET`은 뒤쪽 페이지로 갈수록 앞의 행들을 건너뛰느라 느려집니다(수천만 행에서 특히).
그래서 **마지막으로 본 `id`를 커서로 넘겨** 다음 페이지를 가져오는 방식으로 구현했습니다.

```sql
-- OFFSET 방식(느려짐)
... ORDER BY id DESC LIMIT 20 OFFSET 1000000;

-- 커서 방식(일정한 성능)
... WHERE id < :cursor ORDER BY id DESC LIMIT 20;
```

- 응답의 `nextCursor`를 다음 요청 `cursor`로 넘기면 이어서 조회됩니다.
- `size + 1`을 조회해, 마지막 페이지 여부(`hasNext`)를 **추가 COUNT 쿼리 없이** 판단합니다.
- 구현: `MeterReadingRepository.findPageByDevice(...)`, `MeterReadingService`

### 2. 월별 집계는 요약 테이블에서 조회
대시보드의 월별 사용량 합계는 매번 원본을 집계하지 않고, 트리거+배치로 유지되는
`monthly_meter_summary`(작은 테이블)를 읽습니다. 복합 키 `(device_id, ym)`는 JPA `@EmbeddedId`로 매핑했습니다.

---

## API

| 메서드 | 경로 | 설명 |
|---|---|---|
| GET | `/api/devices/{deviceId}/readings?cursor={id}&size={n}` | 단말기별 검침 이력(커서 페이지네이션, 최신순) |
| GET | `/api/summary/monthly?ym=2024-03` | 특정 월의 단말기별 사용량 합계(요약 테이블) |

예시:
```bash
curl "http://localhost:8080/api/devices/1/readings?size=20"
curl "http://localhost:8080/api/devices/1/readings?size=20&cursor=123456"
curl "http://localhost:8080/api/summary/monthly?ym=2024-03"
```

응답 예(검침 이력):
```json
{
  "content": [
    { "id": 123456, "deviceId": 1, "readingAt": "2024-03-31T23:45:00", "usageValue": 1.234 }
  ],
  "nextCursor": 123437,
  "hasNext": true
}
```

---

## 실행 방법

1. **DB 준비.** 앞 레포([mariadb-query-optimization](https://github.com/hanbbi/mariadb-query-optimization))로
   MariaDB를 띄우고 스키마 생성 + 데이터 적재를 먼저 완료하세요.
   (이 레포에도 동일한 `docker-compose.yml`이 있어 `docker compose up -d`로 같은 DB를 띄울 수 있습니다.
   단, 테이블/데이터는 앞 레포의 `sql/` 스크립트로 만듭니다.)
2. **접속 정보 확인.** `src/main/resources/application.yml`이 `localhost:3307 / qopt / root`를 바라봅니다.
   앞 레포의 docker-compose와 동일하게 맞춰져 있습니다.
3. **실행.**
   ```bash
   ./gradlew bootRun      # 또는 IntelliJ 에서 MeterApiApplication 실행
   ```

> `ddl-auto: none` 입니다. 테이블은 앞 레포에서 만들고, 이 앱은 조회만 합니다.
> Gradle Wrapper 파일이 없다면 `gradle wrapper`로 생성하거나 IDE로 열면 자동 임포트됩니다.

---

## 프로젝트 구조
```
src/main/java/com/hanbi/meterapi
├── MeterApiApplication.java
├── domain/        # MeterReading, MonthlyMeterSummary(@EmbeddedId)
├── repository/    # 커서 페이지네이션 JPQL, 월별 요약 조회
├── service/       # 조회 로직 (size+1 로 hasNext 판단)
├── controller/    # REST 엔드포인트
└── dto/           # 응답 record (CursorPageResponse 등)
```
