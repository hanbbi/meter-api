package com.hanbi.meterapi.controller;

import com.hanbi.meterapi.dto.CursorPageResponse;
import com.hanbi.meterapi.dto.MeterReadingResponse;
import com.hanbi.meterapi.service.MeterReadingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class MeterReadingController {

    private final MeterReadingService service;

    /** 예: GET /api/devices/1/readings?size=20&cursor=123456 */
    @GetMapping("/{deviceId}/readings")
    public CursorPageResponse<MeterReadingResponse> getReadings(
            @PathVariable Integer deviceId,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "20") int size) {

        int capped = Math.min(Math.max(size, 1), 100);   // 페이지 크기 상·하한
        return service.getReadings(deviceId, cursor, capped);
    }
}
