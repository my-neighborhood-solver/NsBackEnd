package com.zerobase.nsbackend.chatting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ChattingRoomCreateRequest {

  private Long errand_Id;
  private Long sender_Id;
}
