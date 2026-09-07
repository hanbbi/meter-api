package com.hanbi.meterapi.dto;

import com.hanbi.meterapi.domain.MeterReading;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MeterReadingResponse(
        Long id,
        Integer deviceId,
        LocalDateTime readingAt,
        BigDecimal usageValue) {

    public static MeterReadingResponse from(MeterReading r) {
        return new MeterReadingResponse(r.getId(), r.getDeviceId(), r.getReadingAt(), r.getUsageValue());
    }
}
