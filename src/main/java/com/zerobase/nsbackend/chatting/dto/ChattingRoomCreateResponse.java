package com.zerobase.nsbackend.chatting.dto;

import com.zerobase.nsbackend.chatting.domain.entity.ChattingRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ChattingRoomCreateResponse {

  private Long ChattingRoom_Id;
  private Long Errand_Id;
  private Long sender_Id;

  public static ChattingRoomCreateResponse from(ChattingRoom chattingRoom) {
    return ChattingRoomCreateResponse.builder()
        .ChattingRoom_Id(chattingRoom.getId())
        .Errand_Id(chattingRoom.getErrand().getId())
        .sender_Id(chattingRoom.getSender().getId())
        .build();
  }
}
