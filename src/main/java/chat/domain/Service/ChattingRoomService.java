package chat.domain.Service;


import chat.domain.entity.ChattingRoom;
import chat.domain.entity.ChattingRoomMember;
import chat.domain.repository.ChattingContentRepository;
import chat.domain.repository.ChattingRoomRepository;
import chat.dto.ChattingRoomRequestDTO;
import chat.exception.CustomException;
import chat.exception.ErrorCode;
import java.util.ArrayList;
import java.util.List;
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
    ChattingRoomRequestDTO extractedDto = ChattingRoomRequestDTO.toCreateDto(dto);
    Long errandId = extractedDto.getErrandsId();
    Long membersId = extractedDto.getMembersId();
    String title = extractedDto.getTitle();

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
  }


  private ChattingRoom createChattingRoom(Errand errand, Members members, String title) {
    ChattingRoom chattingRoom = ChattingRoom.builder()
        .errand(errand)
        .title(title)
        .status("USE")
        .build();

    // ChattingRoomMember 생성 및 리스트에 추가
    ChattingRoomMember roomMember = ChattingRoomMember.builder()
        .chattingRoom(chattingRoom)
        .members(members)
        .build();

    List<ChattingRoomMember> roomMemberList = new ArrayList<>();
    roomMemberList.add(roomMember);
    chattingRoom.setRoomMemberList(roomMemberList);

    return chattingRoomRepository.save(chattingRoom);
  }
}
