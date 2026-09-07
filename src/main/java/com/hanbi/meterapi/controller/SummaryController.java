package com.hanbi.meterapi.controller;

import com.hanbi.meterapi.dto.MonthlySummaryResponse;
import com.hanbi.meterapi.service.SummaryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/summary")
@RequiredArgsConstructor
public class SummaryController {

    private final SummaryService service;

    /** 예: GET /api/summary/monthly?ym=2024-03 */
    @GetMapping("/monthly")
    public List<MonthlySummaryResponse> getMonthly(@RequestParam String ym) {
        return service.getMonthly(ym);
    }
}
