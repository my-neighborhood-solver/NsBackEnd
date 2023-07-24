package chat.domain.repository;

import chat.domain.entity.ChattingRoomMember;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChattingRoomMemberRepository extends JpaRepository<ChattingRoomMember, Long> {

  List<ChattingRoomMember> findByChattingRoomId(Long chattingRoomId);

}
