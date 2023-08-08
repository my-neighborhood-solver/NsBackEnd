package com.zerobase.nsbackend.chatting.controller;

import com.zerobase.nsbackend.chatting.domain.entity.ChattingContent;
import com.zerobase.nsbackend.chatting.domain.service.ChattingRoomService;
import com.zerobase.nsbackend.chatting.dto.ChatContentResponse;
import com.zerobase.nsbackend.chatting.dto.ChattingRoomAllResponse;
import com.zerobase.nsbackend.chatting.dto.ChattingRoomCreateRequest;
import com.zerobase.nsbackend.chatting.dto.ChattingRoomCreateResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    return ResponseEntity.ok().body(chattingRoomService
        .createChattingRoom(request.getErrandId(), request.getSenderId()));
  }

  //채팅방 전체조회
  @GetMapping("/memberId={memberId}")
  public ResponseEntity<List<ChattingRoomAllResponse>> getChattingRooms(
      @PathVariable Long memberId) {
    List<ChattingRoomAllResponse> chattingRooms = chattingRoomService
        .getChattingRoomsByMemberId(memberId);
    return ResponseEntity.ok(chattingRooms);
  }

  // 채팅방 단건 조회
  @GetMapping("/{roomId}/memberId={memberId}")
  public ResponseEntity<List<ChatContentResponse>> getChattingRoom(@PathVariable Long roomId,
      @PathVariable Long memberId) {
    return ResponseEntity.ok().body(chattingRoomService
        .getChattingRoomByIdAndMemberId(roomId, memberId));
  }
}