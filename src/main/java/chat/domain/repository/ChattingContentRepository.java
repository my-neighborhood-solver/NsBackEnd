package chat.domain.repository;

import chat.domain.entity.ChattingContent;
import chat.domain.entity.ChattingRoom;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChattingContentRepository extends JpaRepository<ChattingContent, Long> {


  ChattingContent findTopByChattingRoomOrderByCreatedAtDesc(ChattingRoom chattingRoom);

  List<ChattingContent> findByChattingRoomIdOrderByCreatedAtDesc(Long chattingRoomId);

  List<ChattingContent> findByChattingRoom(ChattingRoom chattingRoom);
}
