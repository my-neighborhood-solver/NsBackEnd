package com.zerobase.nsbackend.member.exception;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class ErrorResponse {
    private final String message;
    private final LocalDateTime time;
    private final UUID logId;

    public static ErrorResponse of(UUID logId, Exception ex) {
        return ErrorResponse.builder()
            .message(ex.getMessage())
            .time(LocalDateTime.now())
            .logId(logId)
            .build();
    }
}
