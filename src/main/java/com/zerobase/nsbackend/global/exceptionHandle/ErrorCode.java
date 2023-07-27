package com.zerobase.nsbackend.global.exceptionHandle;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

  /**
   * 공통 에러 메세지
   * 규칙 : 1000 ~ 19999
   */
  INTERNAL_SERVER_ERROR("1000", "일시적인 장애 입니다."),
  FAIL_TO_UPLOAD_FILE("1001", "파일 업로드에 실패했습니다."),
  /**
   * 회원 도메인 에러 메세지
   * 규칙 : 2000 ~ 2999
   */
  MEMBER_NOT_FOUND("2000", "회원을 찾지 못했습니다."),

  /**
   * 의뢰 도메인 에러 메세지
   * 규칙 : 3000 ~ 3999
   */
  ERRAND_NOT_FOUND("3000", "의뢰를 찾지 못했습니다."),
  /**
   * 채팅 도메인 에러 메세지
   * 규칙 : 4000 ~ 4999
   */
  CHATTING_NOT_FOUND("4000", "채팅을 찾지 못했습니다.");




  private final String code;
  private final String description;

  private static final Map<String, ErrorCode> ERROR_CODE_MAP = new HashMap<>();

  static {
    for (ErrorCode errorCode : ErrorCode.values()) {
      ERROR_CODE_MAP.put(errorCode.description, errorCode);
    }
  }

  public static ErrorCode findCode(String description) {
    return ERROR_CODE_MAP.get(description);
  }
}
