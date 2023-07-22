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
  private String title;


  public static ChattingRoomRequestDTO toCreateDto(ChattingRoomRequestDTO dto) {
    ChattingRoomRequestDTO newDto = new ChattingRoomRequestDTO();
    newDto.errandsId = dto.getErrandsId();
    newDto.membersId = dto.getMembersId();
    newDto.title = dto.getTitle();
    return newDto;
  }
}
