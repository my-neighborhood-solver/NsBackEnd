package chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChattingContentRequestDTO {

  private Long roomId;
  private Long senderId;
  private String content;



  public static ChattingContentRequestDTO fromChattingRoomRequestDTO(ChattingContentRequestDTO dto){
    ChattingContentRequestDTO requestDTO = new ChattingContentRequestDTO();
    requestDTO.roomId = dto.getRoomId();
    requestDTO.senderId = dto.getSenderId();
    requestDTO.content = dto.getContent();
    return requestDTO;
  }
}
