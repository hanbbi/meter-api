package com.hanbi.meterapi.repository;

import com.hanbi.meterapi.domain.MonthlyMeterSummary;
import com.hanbi.meterapi.domain.MonthlyMeterSummaryId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MonthlyMeterSummaryRepository
        extends JpaRepository<MonthlyMeterSummary, MonthlyMeterSummaryId> {

    @Query("SELECT s FROM MonthlyMeterSummary s WHERE s.id.ym = :ym ORDER BY s.id.deviceId")
    List<MonthlyMeterSummary> findByYm(@Param("ym") String ym);
}
