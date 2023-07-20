package chat.domain.Service;

import chat.domain.entity.ChattingContent;
import chat.domain.entity.ChattingRoom;
import chat.domain.repository.ChattingContentRepository;
import chat.domain.repository.ChattingRoomRepository;
import chat.exception.CustomException;
import chat.exception.ErrorCode;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChattingService {

  private final ChattingContentRepository chattingContentRepository;
  private final ChattingRoomRepository chattingRoomRepository;

  @Transactional
  public void saveContent(ChattingContent content,Long roomId) {
    Optional<ChattingRoom> room = chattingRoomRepository.findById(roomId);
    if(room.isPresent()) {
      chattingContentRepository.save(ChattingContent.builder()
          .chattingRoom(room.get())
          .senderId(content.getSenderId())
          .content(content.getContent())
          .isRead(content.getIsRead())
          .build());
    }
    else{
      new CustomException(ErrorCode.NOT_FOUNT_CHATTINGROOM);
    }

  }
}
