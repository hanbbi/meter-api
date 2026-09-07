package com.hanbi.meterapi.service;

import com.hanbi.meterapi.domain.MeterReading;
import com.hanbi.meterapi.dto.CursorPageResponse;
import com.hanbi.meterapi.dto.MeterReadingResponse;
import com.hanbi.meterapi.repository.MeterReadingRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MeterReadingService {

    private final MeterReadingRepository repository;

    public CursorPageResponse<MeterReadingResponse> getReadings(Integer deviceId, Long cursor, int size) {
        // size+1 을 가져와서, 마지막 페이지 여부(hasNext)를 추가 쿼리 없이 판단합니다.
        List<MeterReading> rows = repository.findPageByDevice(deviceId, cursor, PageRequest.of(0, size + 1));

        boolean hasNext = rows.size() > size;
        List<MeterReading> page = hasNext ? rows.subList(0, size) : rows;

        List<MeterReadingResponse> content = page.stream()
                .map(MeterReadingResponse::from)
                .toList();

        Long nextCursor = hasNext ? page.get(page.size() - 1).getId() : null;
        return new CursorPageResponse<>(content, nextCursor, hasNext);
    }
}
