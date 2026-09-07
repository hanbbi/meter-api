package com.hanbi.meterapi.dto;

import com.hanbi.meterapi.domain.MonthlyMeterSummary;
import java.math.BigDecimal;

public record MonthlySummaryResponse(
        Integer deviceId,
        String ym,
        BigDecimal totalUsage,
        Integer readingCount) {

    public static MonthlySummaryResponse from(MonthlyMeterSummary s) {
        return new MonthlySummaryResponse(
                s.getId().getDeviceId(),
                s.getId().getYm(),
                s.getTotalUsage(),
                s.getReadingCount());
    }
}
