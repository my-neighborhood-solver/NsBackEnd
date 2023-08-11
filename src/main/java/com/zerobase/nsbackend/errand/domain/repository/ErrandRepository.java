package com.zerobase.nsbackend.errand.domain.repository;

import com.zerobase.nsbackend.errand.domain.entity.Errand;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ErrandRepository extends JpaRepository<Errand, Long> {
  @Query("select e "
      + "from Errand e"
      + " left join fetch e.images "
      + " left join fetch e.hashtags "
      + " where e.id = :id")
  Optional<Errand> findErrandWithImagesAndHashTagById(@Param("id") Long id);

  @Query("select distinct e "
      + "from Errand e"
      + " left join fetch e.images "
      + " left join fetch e.hashtags ")
  List<Errand> findErrandAllWithImagesAndHashTag();

  @Query("select count(e) from Errand e where e.errander.id = :memberId")
  Integer countByErranderId(Long memberId);
}
