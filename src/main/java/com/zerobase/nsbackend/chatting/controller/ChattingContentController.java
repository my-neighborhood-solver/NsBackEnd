package com.zerobase.nsbackend.chatting.controller;

import com.zerobase.nsbackend.chatting.domain.service.ChattingContentService;
import com.zerobase.nsbackend.chatting.domain.service.ChattingRoomService;
import com.zerobase.nsbackend.chatting.dto.ChatContentRequest;
import com.zerobase.nsbackend.chatting.dto.ChatContentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@PreAuthorize("hasRole('USER')")
@Controller
@RequiredArgsConstructor
public class ChattingContentController {

  private final ChattingContentService chattingContentService;
  private final ChattingRoomService chattingRoomService;

  @MessageMapping("/sendMessage")
  public ResponseEntity<ChatContentResponse> sendMessage(@Payload ChatContentRequest request) {

    chattingRoomService.markUnreadChattingContentAsRead(request.getChattingRoomId(),
        request.getSenderId());
    return ResponseEntity.ok().body(chattingContentService.saveChattingContent(request));
  }
}
