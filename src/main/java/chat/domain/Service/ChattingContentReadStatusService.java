package chat.domain.Service;


import chat.domain.entity.ChattingContent;
import chat.domain.entity.ChattingContentReadStatus;
import chat.domain.entity.ChattingRoom;
import chat.domain.repository.ChattingContentReadStatusRepository;
import chat.domain.repository.ChattingContentRepository;
import chat.domain.repository.ChattingRoomRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChattingContentReadStatusService {

  private final ChattingContentReadStatusRepository chattingContentReadStatusRepository;
  private final ChattingRoomRepository chattingRoomRepository;
  private final ChattingContentRepository chattingContentRepository;


  @Transactional
  public void saveChattingContentReadStatus(ChattingContent content) {
    ChattingContentReadStatus status = ChattingContentReadStatus.builder()
        .chattingContent(content)
        .member(content.getSender())
        .isRead(false)
        .build();
    chattingContentReadStatusRepository.save(status);
  }

  // 읽음 처리
  @Transactional
  public void markAsRead(Long room_Id, Long member_Id) {
    Optional<ChattingRoom> chattingRoom = chattingRoomRepository.findById(room_Id);
    if (chattingRoom.isPresent()) {
      List<ChattingContent> contents = chattingContentRepository
          .findByChattingRoom(chattingRoom.get());
      for (ChattingContent content : contents) {
        List<ChattingContentReadStatus> readStatusList = content.getChattingContentReadStatuses()
            .stream()
            .filter(status -> status.getMember().getId().equals(member_Id) && !status.isRead())
            .collect(Collectors.toList());

        for (ChattingContentReadStatus readStatus : readStatusList) {
          readStatus.setRead(true);
        }
        chattingContentReadStatusRepository.saveAll(readStatusList);
      }
    } else {
      throw new RuntimeException("ChattingRoom not found");
    }


  }
}
