package com.zerobase.nsbackend.chatting.domain.repository;

import com.zerobase.nsbackend.chatting.domain.entitiy.ChattingContent;
import com.zerobase.nsbackend.member.domain.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChattingContentRepository extends JpaRepository<ChattingContent, Long> {

  Long countBySenderNotAndIsRead(Member memberEntity, boolean b);

  List<ChattingContent> findByChattingRoomIdOrderByContentTimeDesc(Long roomId);
}
