package chat.dto;

import chat.domain.entity.ChattingContent;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChattingRoomContentResponseDTO {

  private Long contentId;
  private String senderNickName;
  private String content;
  private LocalDateTime Time;
  private int unReadCount;


  public static ChattingRoomContentResponseDTO fromChattingContent(ChattingContent chattingContent, int unReadCount ){
    ChattingRoomContentResponseDTO responseDTO = new ChattingRoomContentResponseDTO();
    responseDTO.contentId = chattingContent.getId();
    responseDTO.senderNickName = chattingContent.getSenderNickName();
    responseDTO.content = chattingContent.getContent();
    responseDTO.Time = chattingContent.getCreateAt();
    responseDTO.unReadCount = unReadCount;

    return responseDTO;
  }
}
