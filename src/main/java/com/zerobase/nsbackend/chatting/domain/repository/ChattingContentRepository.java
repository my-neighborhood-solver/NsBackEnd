package com.zerobase.nsbackend.chatting.domain.repository;

import com.zerobase.nsbackend.chatting.domain.entity.ChattingContent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChattingContentRepository extends JpaRepository<ChattingContent, Long> {


}
