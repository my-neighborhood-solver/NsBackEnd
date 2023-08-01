package com.zerobase.nsbackend.chatting.domain.repository;

import com.zerobase.nsbackend.chatting.domain.entity.ChattingContent;

import com.zerobase.nsbackend.member.domain.Member;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChattingContentRepository extends JpaRepository<ChattingContent, Long> {

  int countBySenderNotAndIsRead(Member memberEntity, boolean b);

  List<ChattingContent> findByChattingRoomIdOrderByContentTimeDesc(Long roomId);

  // chattingRoomId가 일치하고 isRead가 false이며 sender가 memberId와 다른 경우의 ChattingContent들을 읽음 처리로 변경
  @Transactional
  @Modifying
  @Query("UPDATE ChattingContent c " +
      "SET c.isRead = true " +
      "WHERE c.isRead = false " +
      "AND c.chattingRoom.id = :chattingRoomId " +
      "AND c.sender.id <> :memberId")
  void markAsReadInChattingRoomExceptSender(Long chattingRoomId, Long memberId);

  List<ChattingContent> findByChattingRoomId(Long chattingRoomId);
}
