package com.zerobase.nsbackend.errand.domain.repository;

import com.zerobase.nsbackend.errand.domain.entity.Errand;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ErrandRepository extends JpaRepository<Errand, Long>, ErrandQueryDslRepository{

  @Query("select distinct e "
      + " from Errand e "
      + " left join fetch e.errander "
      + " left join fetch e.hashtags")
  List<Errand> findWithFetchAll();

  @Query("select distinct e "
      + " from Errand e "
      + " left join fetch e.errander"
      + " left join fetch e.hashtags"
      + " where e.id = :errandId")
  Optional<Errand> findWithFetchById(Long errandId);


  @Query("select count(e) from Errand e where e.errander.id = :memberId")
  Integer countByErranderId(Long memberId);
}
