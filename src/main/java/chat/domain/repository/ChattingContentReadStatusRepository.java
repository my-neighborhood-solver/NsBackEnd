package chat.domain.repository;

import chat.domain.entity.ChattingContent;
import chat.domain.entity.ChattingContentReadStatus;
import chat.domain.entity.ChattingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChattingContentReadStatusRepository extends JpaRepository<ChattingContentReadStatus, Long> {

  int countByChattingContent_ChattingRoomAndMember_IdAndIsReadFalse(ChattingRoom chattingRoom, Long memberId);

  int countByChattingContentAndIsRead(ChattingContent chattingContent, boolean b);

}
