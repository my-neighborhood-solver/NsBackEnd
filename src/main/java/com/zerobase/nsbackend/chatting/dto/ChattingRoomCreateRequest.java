package com.zerobase.nsbackend.chatting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ChattingRoomCreateRequest {

  private Long errandId;
  private Long senderId;
}
