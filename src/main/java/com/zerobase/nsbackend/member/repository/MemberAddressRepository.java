package com.zerobase.nsbackend.member.repository;

import com.zerobase.nsbackend.member.domain.Member;
import com.zerobase.nsbackend.member.domain.MemberAddress;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberAddressRepository extends JpaRepository<MemberAddress, Long> {
    Optional<MemberAddress> findAllByMember(Member member);
    boolean existsByMember(Member member);
}
