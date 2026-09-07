package com.hanbi.meterapi.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** 시계열 검침 원본. mariadb-query-optimization 레포의 meter_reading 테이블과 매핑됩니다. */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "meter_reading")
public class MeterReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_id", nullable = false)
    private Integer deviceId;

    @Column(name = "reading_at", nullable = false)
    private LocalDateTime readingAt;

    @Column(name = "usage_value", nullable = false, precision = 12, scale = 3)
    private BigDecimal usageValue;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
