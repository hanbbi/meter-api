package com.hanbi.meterapi.dto;

import java.util.List;

/** 커서 페이지네이션 응답. nextCursor 를 다음 요청의 cursor 로 넘기면 이어서 조회됩니다. */
public record CursorPageResponse<T>(
        List<T> content,
        Long nextCursor,
        boolean hasNext) {
}
