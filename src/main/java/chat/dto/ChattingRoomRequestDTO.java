package chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChattingRoomRequestDTO {

  private String type;
  private Long errandsId;
  private Long membersId;


  public static ChattingRoomRequestDTO toCreateDto(ChattingRoomRequestDTO dto) {
    ChattingRoomRequestDTO newDto = new ChattingRoomRequestDTO();
    newDto.type = dto.getType();
    newDto.errandsId = dto.getErrandsId();
    newDto.membersId = dto.getMembersId();
    return newDto;
  }
}
