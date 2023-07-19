package com.zerobase.nsbackend.member.repository;

import com.zerobase.nsbackend.member.domain.Members;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Members,Long> {
    Optional<Members> findByEmail(String email);
}
