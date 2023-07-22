package chat.web;

import chat.domain.Service.ChattingRoomService;
import chat.domain.entity.ChattingRoom;
import chat.domain.repository.ChattingRoomRepository;
import chat.dto.ChattingRoomRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chats")
public class ChattingRoomController {

  private final ChattingRoomService chattingRoomService;

  // 채팅방 생성
  @PostMapping
  public ResponseEntity<ChattingRoom> createChatRoom(
      @Header String OneToOne,
      @RequestBody ChattingRoomRequestDTO dto){
    return ResponseEntity.ok(chattingRoomService.createChattingRoom(dto));
  };


}
