package chat.dto;

import chat.domain.entity.ChattingContent;
import chat.domain.entity.ChattingRoom;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChattingRoomResponseDTO {

  private Long roomId;
  private String type;
  private String title;
  private String status;
  private String lastSender;
  private String lastContent;
  private LocalDateTime lastMessageTime;
  private int unReadCount;

  public static ChattingRoomResponseDTO fromChattingRoom(ChattingRoom chattingRoom, ChattingContent content, int unReadCount) {
    ChattingRoomResponseDTO responseDTO = new ChattingRoomResponseDTO();
    responseDTO.roomId = chattingRoom.getId();
    responseDTO.type = chattingRoom.getType();
    responseDTO.title = chattingRoom.getErrand().getTitle();
    responseDTO.status = chattingRoom.getStatus();

    if (content != null) {
      responseDTO.lastSender = content.getSenderNickName();
      responseDTO.lastContent = content.getContent();
      responseDTO.lastMessageTime = content.getCreateAt();
    }

    responseDTO.unReadCount = unReadCount;

    return responseDTO;
  }
}
