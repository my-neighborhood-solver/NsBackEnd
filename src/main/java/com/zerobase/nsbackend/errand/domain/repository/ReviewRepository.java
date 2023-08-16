package com.zerobase.nsbackend.errand.domain.repository;

import com.zerobase.nsbackend.errand.domain.entity.Review;
import com.zerobase.nsbackend.member.domain.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByReviewee(Member member);
}
