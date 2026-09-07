package com.hanbi.meterapi.repository;

import com.hanbi.meterapi.domain.MeterReading;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MeterReadingRepository extends JpaRepository<MeterReading, Long> {

    /**
     * 커서(no-offset) 페이지네이션.
     * OFFSET 을 쓰지 않고 마지막으로 본 id 를 기준(cursor)으로 다음 페이지를 가져옵니다.
     * 뒤쪽 페이지로 갈수록 느려지는 OFFSET 방식과 달리, 데이터가 수천만 행이어도 일정한 성능을 냅니다.
     * cursor 가 null 이면 가장 최신부터 시작합니다.
     */
    @Query("""
            SELECT r FROM MeterReading r
            WHERE r.deviceId = :deviceId
              AND (:cursor IS NULL OR r.id < :cursor)
            ORDER BY r.id DESC
            """)
    List<MeterReading> findPageByDevice(@Param("deviceId") Integer deviceId,
                                        @Param("cursor") Long cursor,
                                        Pageable pageable);
}
