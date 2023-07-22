package chat.domain.repository;

import chat.domain.entity.ChattingContentReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChattingContentReadStatusRepository extends JpaRepository<ChattingContentReadStatus, Long> {

}
