package com.zerobase.nsbackend.chatting.domain.repository;

import com.zerobase.nsbackend.chatting.domain.entity.ChattingContent;
import com.zerobase.nsbackend.chatting.domain.entity.ChattingRoom;
import com.zerobase.nsbackend.member.domain.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChattingContentRepository extends JpaRepository<ChattingContent, Long> {

  List<ChattingContent> findByChattingRoom_IdOrderByCreatedAtDesc(Long roomId);

  int countBySenderNotAndIsReadAndChattingRoom(Member member1, boolean b, ChattingRoom chattingRoom1);
}
