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
  INVALID_INPUT_ERROR("1002","올바르지 않은 입력입니다."),
  /**
   * 회원 도메인 에러 메세지
   * 규칙 : 2000 ~ 2999
   */
  MEMBER_NOT_FOUND("2000", "회원을 찾지 못했습니다."),
  EMAIL_EXIST("2001","해당하는 이메일이 존재합니다."),
  NO_EXIST_EMAIL("2002","존재하지 않는 사용자입니다."),
  NO_MATCH_PASSWORD("2002","비밀번호가 일치하지 않습니다."),
  IS_SOCIAL_LOGIN("2003","소셜 로그인으로 가입한 이메일입니다."),
  NOT_FOUND_USER("2004","사용자 정보를 찾을 수 없습니다."),
  IS_NOT_SOCAIL_USER("2005","소셜 로그인으로 가입한 계정이 아닙니다."),
  NO_EXIST_DATA("2006","존재하지 않는 데이터입니다."),
  IS_DELETED_USER("2007","회원탈퇴 진행중인 회원입니다."),
  EXIST_INTEREST_BOARD("2008","이미 존재하는 관심글입니다."),
  NO_EXIST_INTEREST_BOARD("2009","관심글이 아닌 의뢰입니다."),

  /**
   * 의뢰 도메인 에러 메세지
   * 규칙 : 3000 ~ 3999
   */
  ERRAND_NOT_FOUND("3000", "의뢰를 찾지 못했습니다."),
  DONT_HAVE_AUTHORITY_TO_EDIT("3001", "수정할 권한이 없습니다."),
  DONT_HAVE_AUTHORITY_TO_DELETE("3002", "삭제할 권한이 없습니다."),
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
