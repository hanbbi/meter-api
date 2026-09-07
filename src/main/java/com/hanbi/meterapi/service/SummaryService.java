package com.hanbi.meterapi.service;

import com.hanbi.meterapi.dto.MonthlySummaryResponse;
import com.hanbi.meterapi.repository.MonthlyMeterSummaryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SummaryService {

    private final MonthlyMeterSummaryRepository repository;

    public List<MonthlySummaryResponse> getMonthly(String ym) {
        return repository.findByYm(ym).stream()
                .map(MonthlySummaryResponse::from)
                .toList();
    }
}
