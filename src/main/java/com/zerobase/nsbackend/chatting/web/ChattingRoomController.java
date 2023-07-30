package com.zerobase.nsbackend.chatting.web;

import com.zerobase.nsbackend.chatting.domain.entitiy.ChattingContent;
import com.zerobase.nsbackend.chatting.domain.service.ChattingRoomService;
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
  public ResponseEntity<ChattingRoomCreateResponse> createChattingRoom(@RequestBody ChattingRoomCreateRequest request) {

    //conflict 충돌로 이미 생성되어 있는 채팅방  success 생성된 채팅방

    return chattingRoomService
        .createChattingRoom(request.getErrand_Id(), request.getSender_Id());
  }

  //채팅방 전체조회
  @GetMapping("/{memberId}")
  public ResponseEntity<List<ChattingRoomAllResponse>> getChattingRooms(@PathVariable Long memberId) {
    List<ChattingRoomAllResponse> chattingRooms = chattingRoomService.getChattingRoomsByMemberId(memberId);
    return ResponseEntity.ok(chattingRooms);
  }

  // 채팅방 단건 조회
  @GetMapping("/{roomId}/members/{memberId}")
  public ResponseEntity<List<ChattingContent>> getChattingRoom(@PathVariable Long roomId, @PathVariable Long memberId) {
    List<ChattingContent> chattingRoom = chattingRoomService.getChattingRoomByIdAndMemberId(roomId, memberId);
    return ResponseEntity.ok().body(chattingRoom);
  }
}
