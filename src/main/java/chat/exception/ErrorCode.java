package chat.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter

public enum ErrorCode {

  NOT_FOUND_ERRAND(HttpStatus.BAD_REQUEST, "게시물이 없습니다."),
  NOT_FOUND_MEMBERS(HttpStatus.BAD_REQUEST, "회원이 존재하지않습니다."),
  NOT_FOUNT_CHATTINGROOM(HttpStatus.BAD_REQUEST, " 채팅방이 존재하지않습니다.");

  private final HttpStatus httpStatus;
  private final String detail;
}
