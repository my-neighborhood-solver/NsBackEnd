package chat.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChattingContentRequestDto {

  private Long roomId;
  private Long senderId;
  private String content;
  private LocalDateTime Time;

}
