package chat.domain.Service;


import chat.domain.entity.ChattingContent;
import chat.domain.entity.ChattingRoom;
import chat.domain.entity.ChattingRoomMember;
import chat.domain.repository.ChattingContentReadStatusRepository;
import chat.domain.repository.ChattingContentRepository;
import chat.domain.repository.ChattingRoomMemberRepository;
import chat.domain.repository.ChattingRoomRepository;
import chat.dto.ChattingRoomContentResponseDTO;
import chat.dto.ChattingRoomRequestDTO;
import chat.dto.ChattingRoomResponseDTO;
import chat.exception.CustomException;
import chat.exception.ErrorCode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChattingRoomService {

  private final ChattingRoomRepository chattingRoomRepository;
  private final ChattingRoomMemberRepository chattingRoomMemberRepository;
  private final ChattingContentRepository chattingContentRepository;
  private final ChattingContentReadStatusRepository chattingContentReadStatusRepository;
  private final ErrandRepository errandRepository;
  private final MemberRepository memberRepository;


  @Transactional
  public ChattingRoom createChattingRoom(ChattingRoomRequestDTO dto) {
    ChattingRoomRequestDTO extractedDto = ChattingRoomRequestDTO.toCreateDto(dto);
    String type = extractedDto.getType();
    Long errandId = extractedDto.getErrandsId();
    Long memberId = extractedDto.getMemberId();

    Optional<ChattingRoom> room = chattingRoomRepository
        .findByTypeAndErrand_IdAndRoomMember_Member_Id(type, errandId, memberId);

    if (room.isPresent()) {
      return room.get();
    }

    Errand errand = errandRepository.findById(errandId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ERRAND));
    Member member = memberRepository.findById(memberId)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBERS));

    List<ChattingRoomMember> roomMemberList = new ArrayList<>();
    addChattingRoomMember(roomMemberList, member);
    addChattingRoomMember(roomMemberList, errand.getMember());

// 채팅방 엔티티 생성 및 저장
    ChattingRoom newChattingRoom = ChattingRoom.builder()
        .type(type)
        .errand(errand)
        .status("USE")
        .roomMember(roomMemberList)
        .build();

    ChattingRoom savedChattingRoom = chattingRoomRepository.save(newChattingRoom);

    return savedChattingRoom;
  }

  public List<ChattingRoomResponseDTO> getAllChattingRoom(Long memberId) {
    List<ChattingRoom> chattingRooms = chattingRoomRepository
        .findByRoomMemberListMemberId(memberId);
    if (chattingRooms.isEmpty()) {
      throw new RuntimeException("Chatting rooms not found");
    }

    List<ChattingRoomResponseDTO> responseDTO = new ArrayList<>();
    for (ChattingRoom chattingRoom : chattingRooms) {
      ChattingContent chattingContent = chattingContentRepository.
          findTopByChattingRoomOrderByCreatedAtDesc(chattingRoom);
      int unreadMessageCount = chattingContentReadStatusRepository
          .countByChattingContent_ChattingRoomAndMember_IdAndIsReadFalse(chattingRoom, memberId);
      responseDTO.add(ChattingRoomResponseDTO
          .fromChattingRoom(chattingRoom, chattingContent, unreadMessageCount));
    }

    return responseDTO;
  }

  // 채팅방 멤버 추가
  @Transactional
  public void addChattingRoomMember(List<ChattingRoomMember> roomMemberList, Member member) {
    ChattingRoomMember roomMember = ChattingRoomMember.builder()
        .chattingRoom(null) // 채팅방이 아직 생성되지 않았으므로 null로 설정
        .member(member) // 멤버 설정
        .build();
    roomMemberList.add(roomMember);
  }

  //단건 조회
  @Transactional
  public List<ChattingRoomContentResponseDTO> getAllChattingContent(Long chattingRoomId) {
    List<ChattingContent> chattingContents = chattingContentRepository
        .findByChattingRoomIdOrderByCreatedAtDesc(chattingRoomId);
    if (chattingContents.isEmpty()) {
      throw new RuntimeException("Chatting rooms not found");
    }

    List<ChattingRoomContentResponseDTO> responseDTO = new ArrayList<>();
    for (ChattingContent chattingContent : chattingContents) {
      int unReadCount = chattingContentReadStatusRepository
          .countByChattingContentAndIsRead(chattingContent, false);
      responseDTO.add(ChattingRoomContentResponseDTO.fromChattingContent(
          chattingContent, unReadCount));
    }

    return responseDTO;
  }


  // 채팅방 나가기
  @Transactional
  public ResponseEntity<String> exitChattingRoom(Long chattingRoomId, Long member_id) {
    Optional<ChattingRoom> optionalChattingRoom = chattingRoomRepository.findById(chattingRoomId);

    if (optionalChattingRoom.isPresent()) {
      ChattingRoom chattingRoom = optionalChattingRoom.get();
      // 멤버를 member_id와 동일한 경우, 해당 멤버를 제거하고 채팅방을 업데이트
      chattingRoom.getRoomMember()
          .removeIf(member -> member.getMember().getId().equals(member_id));
      chattingRoomRepository.save(chattingRoom);
      return ResponseEntity.ok("chattingExit Success");
    } else {
      // 에러
      return ResponseEntity.badRequest().body("Not found chattingRoom");
    }


  }
}
