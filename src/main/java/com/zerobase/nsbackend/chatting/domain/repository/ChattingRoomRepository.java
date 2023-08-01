package com.zerobase.nsbackend.chatting.domain.repository;

import com.zerobase.nsbackend.chatting.domain.entity.ChattingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long> {


}
