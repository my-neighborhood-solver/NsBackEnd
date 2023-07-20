package chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChattingRoomRequestDTO {

  private Long errandsId;
  private Long membersId;
  private String title;

}
