package com.zerobase.nsbackend.member.repository;

import com.zerobase.nsbackend.errand.domain.vo.ErrandStatus;
import com.zerobase.nsbackend.member.domain.InterestBoard;
import com.zerobase.nsbackend.member.domain.Member;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InterestBoardRepository extends JpaRepository<InterestBoard, Long> {

    @Query("select i.member "
        + "from InterestBoard i "
        + "where i.errand.deadLine = :date "
        + "and i.errand.status = :status")
    List<Member> findMemberByErrandDeadLineAndStatus(
        @Param("date") LocalDate date, @Param("status") ErrandStatus status);
}
