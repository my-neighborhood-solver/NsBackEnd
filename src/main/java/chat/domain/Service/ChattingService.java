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
  private final ChattingContentReadStatusService chattingContentReadStatusService;

  @Transactional
  public void saveContent(ChattingContent content) {
    Optional<ChattingRoom> room = chattingRoomRepository.findById(content.getId());
    if (room.isPresent()) {
      chattingContentRepository.save(content);
      chattingContentReadStatusService.saveChattingContentReadStatus(content);

    } else {
      new CustomException(ErrorCode.NOT_FOUNT_CHATTINGROOM);
    }
  }

  public ChattingRoom getChattingRoomById(Long roomId) {
    Optional<ChattingRoom> room = chattingRoomRepository.findById(roomId);
    if (room.isPresent()) {
      return room.get();
    } else {
      throw new RuntimeException("ChattingRoom not found");
    }
  }

}
