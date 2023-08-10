package com.zerobase.nsbackend.chatting.controller;

import com.zerobase.nsbackend.chatting.domain.entity.ChattingContent;
import com.zerobase.nsbackend.chatting.domain.service.ChattingContentService;
import com.zerobase.nsbackend.chatting.domain.service.ChattingRoomService;
import com.zerobase.nsbackend.chatting.dto.ChatContentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChattingContentController {

  private final ChattingContentService chattingContentService;
  private final ChattingRoomService chattingRoomService;

  @MessageMapping("/sendMessage")
  public ResponseEntity<ChattingContent> sendMessage(@Payload ChatContentRequest request){

    chattingRoomService.markUnreadChattingContentAsRead(request.getChattingRoomId(),
        request.getSenderId());
    return ResponseEntity.ok(chattingContentService.saveChattingContent(request));
  }
}
