package com.zerobase.nsbackend.chatting.domain.repository;


import com.zerobase.nsbackend.chatting.domain.entity.ChattingRoom;
import com.zerobase.nsbackend.errand.domain.entity.Errand;
import com.zerobase.nsbackend.member.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long> {

  Optional<ChattingRoom> findByErrandAndSender(Errand errand, Member sender);

  List<ChattingRoom> findByErrand_Errander_IdOrSenderId(Long memberId, Long senderId);
}
