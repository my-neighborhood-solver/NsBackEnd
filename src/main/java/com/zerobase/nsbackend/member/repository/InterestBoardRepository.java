package com.zerobase.nsbackend.member.repository;

import com.zerobase.nsbackend.errand.domain.entity.Errand;
import com.zerobase.nsbackend.member.domain.InterestBoard;
import com.zerobase.nsbackend.member.domain.Member;
import com.zerobase.nsbackend.member.domain.Notification;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterestBoardRepository extends JpaRepository<InterestBoard, Long> {
    List<InterestBoard> findAllByErrand(Errand errand);
}
