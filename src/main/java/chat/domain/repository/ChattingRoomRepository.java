package chat.domain.repository;

import chat.domain.entity.ChattingRoom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long> {

  Optional<ChattingRoom> findByErrandAndMembersAndTitle(Long errandId, Long membersId,
      String title);
}
