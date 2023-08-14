package com.zerobase.nsbackend.global.exceptionHandle;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
  private static final String INFO_LOG_TEMPLATE = "## info : {}, {}";
  private static final String ERROR_LOG_TEMPLATE = "## error : {}, {}";

  private UUID generateLogId(Exception ex) {
    return UUID.randomUUID();
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleIllegalArgumentException(IllegalArgumentException ex) {
    UUID uuid = generateLogId(ex);
    log.info(INFO_LOG_TEMPLATE, uuid, ex.getClass().getSimpleName(), ex);
    return ErrorResponse.of(generateLogId(ex), ex);
  }
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleIllegalArgumentException(MethodArgumentNotValidException ex) {
    UUID uuid = generateLogId(ex);
    log.info(INFO_LOG_TEMPLATE, uuid, ex.getClass().getSimpleName(), ex);
    return ErrorResponse.builder()
        .errorCode(ErrorCode.INVALID_INPUT_ERROR.getCode())
        .description(ErrorCode.INVALID_INPUT_ERROR.getDescription())
        .dateTime(LocalDateTime.now())
        .logId(generateLogId(ex))
        .build();
  }
  @ExceptionHandler(IllegalStateException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleIllegalStateException(IllegalStateException ex) {
    UUID uuid = generateLogId(ex);
    log.info(INFO_LOG_TEMPLATE, uuid, ex.getClass().getSimpleName(), ex);
    return ErrorResponse.of(generateLogId(ex), ex);
  }
  @ExceptionHandler(UsernameNotFoundException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ErrorResponse handleIllegalArgumentException(UsernameNotFoundException ex) {
    UUID uuid = generateLogId(ex);
    log.info(INFO_LOG_TEMPLATE, uuid, ex.getClass().getSimpleName(), ex);
    return ErrorResponse.of(generateLogId(ex), ex);
  }

  @ExceptionHandler(NoSuchElementException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse handleIllegalArgumentException(NoSuchElementException ex) {
    UUID uuid = generateLogId(ex);
    log.error(ERROR_LOG_TEMPLATE, uuid, ex.getClass().getSimpleName(), ex);
    return ErrorResponse.of(generateLogId(ex), ex);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse handleIllegalArgumentException(Exception ex) {
    UUID uuid = generateLogId(ex);
    log.error(ERROR_LOG_TEMPLATE, uuid, ex.getClass().getSimpleName(), ex);
    return ErrorResponse.of(generateLogId(ex), ex);
  }
}
