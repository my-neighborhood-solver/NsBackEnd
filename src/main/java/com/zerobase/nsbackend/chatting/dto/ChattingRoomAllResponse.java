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

  private Long chattingRoom_Id;
  private String title;
  private String nickName;
  private String chattingContent;
  private LocalDateTime time;
  private int readNotCount;


  public static ChattingRoomAllResponse from(ChattingRoom chattingRoom, String content,
      LocalDateTime time, int count) {
    return ChattingRoomAllResponse.builder()
        .chattingRoom_Id(chattingRoom.getId())
        .title(chattingRoom.getErrand().getTitle())
        .nickName(chattingRoom.getSender().getNickname())
        .chattingContent(content)
        .time(time)
        .readNotCount(count)
        .build();
  }
}
