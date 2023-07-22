package com.zerobase.nsbackend.global.exceptionHandle;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ErrorResponse {
  private final String errorCode;
  private final String description;
  private final LocalDateTime dateTime;
  private final UUID logId;

  public static ErrorResponse of(UUID logId, Exception exception) {
    ErrorCode errorcode = ErrorCode.findCode(exception.getMessage());

    return ErrorResponse.builder()
        .errorCode(errorcode.getCode())
        .description(errorcode.getDescription())
        .dateTime(LocalDateTime.now())
        .logId(logId)
        .build();
  }
}
