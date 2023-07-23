package chat.domain.repository;

import chat.domain.entity.ChattingRoom;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long> {

  Optional<ChattingRoom> findByTypeAndErrand_IdAndRoomMember_Member_Id(
      String type, Long errandId, Long memberId);

  List<ChattingRoom> findByRoomMemberListMemberId(Long memberId);
}
