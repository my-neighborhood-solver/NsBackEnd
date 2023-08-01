package com.zerobase.nsbackend.chatting.domain.repository;


import com.zerobase.nsbackend.chatting.domain.entity.ChattingRoom;
import com.zerobase.nsbackend.errand.domain.Errand;
import com.zerobase.nsbackend.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long> {

  ChattingRoom findByErrand_MemberAndSender(Errand errand, Member sender);



}
