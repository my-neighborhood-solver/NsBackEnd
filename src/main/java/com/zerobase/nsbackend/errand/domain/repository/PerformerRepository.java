package com.zerobase.nsbackend.errand.domain.repository;

import com.zerobase.nsbackend.errand.domain.entity.Errand;
import com.zerobase.nsbackend.errand.domain.entity.Performer;
import com.zerobase.nsbackend.member.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PerformerRepository extends JpaRepository<Performer, Long> {

  boolean existsByErrandAndMember(Errand errand, Member member);
  @Query("select count(p.id) > 0 "
      + "from Performer p "
      + "where p.errand = :errand "
      + "and p.member = :member "
      + "and p.status = 'APPROVE'")
  boolean existsByErrandAndApprovedMember(Errand errand, Member member);

  Optional<Performer> findByErrandAndMember(Errand errand, Member member);

  @EntityGraph(attributePaths = {"member"})
  List<Performer> findByErrand(Errand errand);
}
