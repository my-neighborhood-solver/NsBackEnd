package chat.domain.Service;


import chat.domain.entity.ChattingRoom;
import chat.domain.repository.ChattingContentRepository;
import chat.domain.repository.ChattingRoomRepository;
import chat.dto.ChattingRoomRequestDTO;
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
public class ChattingRoomService {

  private final ChattingRoomRepository chattingRoomRepository;
  private final ChattingContentRepository chattingContentRepository;
  private final ErrandRepository errandRepository;
  private final MembersRepository membersRepository;


  @Transactional
  public ChattingRoom createChattingRoom(ChattingRoomRequestDTO dto) {
    Long errandId = dto.getErrandsId();
    Long membersId = dto.getMembersId();
    String title = dto.getTitle();

    Optional<ChattingRoom> room = chattingRoomRepository
        .findByErrandAndMembersAndTitle(dto.getErrandsId(), dto.getMembersId(), dto.getTitle());

    if (room.isPresent()) {
      return room.get();
    }

    Errand errand = errandRepository.findById(errandId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ERRAND));
    Members members = membersRepository.findById(membersId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBERS));

    return chattingRoomRepository.save(ChattingRoom.builder()
        .errand(errand)
        .members(members)
        .title(title)
        .status("USE")
        .build());
    ;
  }
}
