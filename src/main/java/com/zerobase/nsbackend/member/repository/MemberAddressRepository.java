package com.zerobase.nsbackend.member.repository;

import com.zerobase.nsbackend.member.domain.MemberAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberAddressRepository extends JpaRepository<MemberAddress, Long> {
}
