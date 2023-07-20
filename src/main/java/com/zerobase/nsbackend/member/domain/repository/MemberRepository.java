package com.zerobase.nsbackend.member.domain.repository;

import com.zerobase.nsbackend.member.domain.Members;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Members,Long> {
    boolean existsByEmail(String email);
    Optional<Members> findById(Long id);
}
