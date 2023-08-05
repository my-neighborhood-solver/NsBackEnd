package com.zerobase.nsbackend.chatting.controller;

import com.zerobase.nsbackend.chatting.domain.service.ChattingRoomService;
import com.zerobase.nsbackend.chatting.dto.ChattingRoomCreateRequest;
import com.zerobase.nsbackend.chatting.dto.ChattingRoomCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chats")
public class ChattingRoomController {

  private final ChattingRoomService chattingRoomService;


  //채팅방 생성
  @PostMapping("")
  public ResponseEntity<ChattingRoomCreateResponse> createChattingRoom(
      @RequestBody ChattingRoomCreateRequest request) {

    //conflict 충돌로 뜨면 이미 생성되어 있는 채팅방  success 생성된 채팅방

    return ResponseEntity.ok().body(chattingRoomService
        .createChattingRoom(request.getErrandId(), request.getSenderId()));
  }
}