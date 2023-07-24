package chat.web;

import chat.domain.Service.ChattingContentReadStatusService;
import chat.domain.Service.ChattingRoomService;
import chat.domain.entity.ChattingRoom;
import chat.dto.ChattingRoomContentResponseDTO;
import chat.dto.ChattingRoomRequestDTO;
import chat.dto.ChattingRoomResponseDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chats")
public class ChattingRoomController {

  private final ChattingRoomService chattingRoomService;
  private final ChattingContentReadStatusService chattingContentReadStatusService;


  // 채팅방 생성
  @PostMapping
  public ResponseEntity<ChattingRoom> createChatRoom(
      @RequestBody ChattingRoomRequestDTO dto) {
    return ResponseEntity.ok(chattingRoomService.createChattingRoom(dto));
  }

  // 채팅방 전체 조회
  @GetMapping("/rooms/{memberId}")
  public ResponseEntity<List<ChattingRoomResponseDTO>> getAllChatRoom(@PathVariable Long memberId) {
    List<ChattingRoomResponseDTO> chattingRoom = chattingRoomService.getAllChattingRoom(memberId);
    return ResponseEntity.ok(chattingRoom);
  }

  // 채팅 단건 조회
  @GetMapping("/room/{chattingRoomId}")
  public ResponseEntity<List<ChattingRoomContentResponseDTO>> getAllChattingContent(
      @PathVariable Long chattingRoomId,
      @RequestParam Long member_Id
  ) {

    // 읽음 처리
    chattingContentReadStatusService.markAsRead(chattingRoomId, member_Id);

    List<ChattingRoomContentResponseDTO> Content = chattingRoomService
        .getAllChattingContent(chattingRoomId);
    return ResponseEntity.ok(Content);
  }

  // 채팅방 Exit,
  @PutMapping("/room/{chattingRoomId}/exit")
  public ResponseEntity<String> exitChattingRoom(
      @PathVariable Long chattingRoomId,
      @RequestParam Long member_Id
  ) {
    return chattingRoomService
        .exitChattingRoom(chattingRoomId, member_Id);
  }

  // 채팅방 삭제


}
