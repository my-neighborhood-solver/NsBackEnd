package com.zerobase.nsbackend.chatting.dto;

import com.zerobase.nsbackend.chatting.domain.entity.ChattingRoom;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ChattingRoomAllResponse {

  private Long ChattingRoom_Id;
  private String ChattingContent;
  private String nickName;
  private LocalDateTime time;
  private int readNotCount;


  public static ChattingRoomAllResponse from(ChattingRoom chattingRoom, String content,
      LocalDateTime time, int count) {
    return ChattingRoomAllResponse.builder()
        .ChattingRoom_Id(chattingRoom.getId())
        .ChattingContent(content)
        .nickName(chattingRoom.getSender().getNickname())
        .time(time)
        .readNotCount(count)
        .build();
  }
}
