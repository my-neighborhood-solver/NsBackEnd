package com.zerobase.nsbackend.errand.domain.repository;

import com.zerobase.nsbackend.errand.domain.entity.Errand;
import com.zerobase.nsbackend.errand.domain.entity.Performer;
import com.zerobase.nsbackend.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerformerRepository extends JpaRepository<Performer, Long> {

  boolean existsByErrandAndMember(Errand errand, Member member);
}
