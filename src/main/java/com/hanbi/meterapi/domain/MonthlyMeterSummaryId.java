package com.hanbi.meterapi.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** monthly_meter_summary 의 복합 기본키 (device_id, ym). */
@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MonthlyMeterSummaryId implements Serializable {

    @Column(name = "device_id")
    private Integer deviceId;

    @Column(name = "ym", length = 7)
    private String ym;      // 'YYYY-MM'
}
