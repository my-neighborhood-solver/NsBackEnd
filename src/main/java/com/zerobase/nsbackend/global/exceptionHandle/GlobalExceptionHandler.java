package com.zerobase.nsbackend.global.exceptionHandle;

import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  private UUID generateLogId(Exception ex) {
    return UUID.randomUUID();
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleIllegalArgumentException(IllegalArgumentException ex) {
    UUID uuid = generateLogId(ex);
    log.info("## info : {}, {}", uuid, ex.getClass().getSimpleName(), ex);
    return ErrorResponse.of(generateLogId(ex), ex);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse handleIllegalArgumentException(Exception ex) {
    UUID uuid = generateLogId(ex);
    log.error("## error : {}, {}", uuid, ex.getClass().getSimpleName(), ex);
    return ErrorResponse.of(generateLogId(ex), ex);
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse handleIllegalArgumentException(UsernameNotFoundException ex) {
    UUID uuid = generateLogId(ex);
    log.error("## error : {}, {}", uuid, ex.getClass().getSimpleName(), ex);
    return ErrorResponse.of(generateLogId(ex), ex);
  }

  @ExceptionHandler(NoSuchElementException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse handleIllegalArgumentException(NoSuchElementException ex) {
    UUID uuid = generateLogId(ex);
    log.error("## error : {}, {}", uuid, ex.getClass().getSimpleName(), ex);
    return ErrorResponse.of(generateLogId(ex), ex);
  }
}
