package com.zerobase.nsbackend.chatting.domain.repository;

import com.zerobase.nsbackend.chatting.domain.entitiy.ChattingRoom;
import com.zerobase.nsbackend.errand.domain.Errand;
import com.zerobase.nsbackend.member.domain.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long> {

  ChattingRoom findByErrand_MemberAndSender(Errand errand, Member sender);

  List<ChattingRoom> findByErrandMemberIdAndSenderMemberId(Long memberId, Long senderId);

}
