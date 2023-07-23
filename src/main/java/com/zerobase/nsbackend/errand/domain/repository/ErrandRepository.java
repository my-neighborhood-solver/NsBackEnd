package com.zerobase.nsbackend.errand.domain.repository;

import com.zerobase.nsbackend.errand.domain.Errand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ErrandRepository extends JpaRepository<Errand, Long> {

}
