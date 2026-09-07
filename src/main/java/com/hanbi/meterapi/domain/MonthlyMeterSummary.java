package com.hanbi.meterapi.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** 월별 요약 테이블. 트리거+배치로 유지되며, 대시보드 조회는 이 작은 테이블만 읽습니다. */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "monthly_meter_summary")
public class MonthlyMeterSummary {

    @EmbeddedId
    private MonthlyMeterSummaryId id;

    @Column(name = "total_usage", nullable = false, precision = 18, scale = 3)
    private BigDecimal totalUsage;

    @Column(name = "reading_count", nullable = false)
    private Integer readingCount;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}
