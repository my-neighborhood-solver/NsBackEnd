package com.zerobase.nsbackend.chatting.dto;

import com.zerobase.nsbackend.chatting.domain.entity.ChattingRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ChattingRoomCreateResponse {

  private Long chattingRoomId;
  private Long errandId;
  private Long senderId;
  private ChattingRoomCreateStatus description;

  public static ChattingRoomCreateResponse from(ChattingRoom chattingRoom, ChattingRoomCreateStatus status) {
    return ChattingRoomCreateResponse.builder()
        .chattingRoomId(chattingRoom.getId())
        .errandId(chattingRoom.getErrand().getId())
        .senderId(chattingRoom.getSender().getId())
        .description(status)
        .build();
  }
}
