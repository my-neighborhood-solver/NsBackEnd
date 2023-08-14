package com.zerobase.nsbackend.errand.domain.repository;

import static org.assertj.core.api.Assertions.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.nsbackend.RepositoryTest;
import com.zerobase.nsbackend.errand.domain.entity.Errand;
import com.zerobase.nsbackend.errand.domain.entity.ErrandHashtag;
import com.zerobase.nsbackend.errand.domain.entity.ErrandImage;
import com.zerobase.nsbackend.errand.domain.vo.ErrandStatus;
import com.zerobase.nsbackend.errand.domain.vo.PayDivision;
import com.zerobase.nsbackend.errand.dto.ErrandSearchCondition;
import com.zerobase.nsbackend.errand.dto.search.ErrandSearchResult;
import com.zerobase.nsbackend.member.domain.Member;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class ErrandQueryDslRepositoryTest extends RepositoryTest {
  @Autowired
  TestEntityManager em;
  @Autowired
  JPAQueryFactory jpaQueryFactory;

  ErrandQueryDslRepository errandQueryDslRepository;

  Member member01;
  Errand errand01;
  @BeforeEach
  void init() {
    errandQueryDslRepository = new ErrandRepositoryImpl(jpaQueryFactory);
     member01 = em.persistAndFlush(Member.builder()
        .nickname("member01")
        .password("pass")
        .email("email")
        .profileImage("/images")
        .build());

    errand01 = em.persistAndFlush(Errand.builder()
        .errander(member01)
        .title("첫번째 의뢰 입니다.")
        .content("content")
        .images(List.of(ErrandImage.of("images/01"), ErrandImage.of("images/02")))
        .hashtags(new HashSet<>(List.of(ErrandHashtag.of("청소"), ErrandHashtag.of("알바"))))
        .pay(10000)
        .payDivision(PayDivision.HOURLY)
        .viewCount(1)
        .status(ErrandStatus.REQUEST)
        .build());

    errand01 = em.persistAndFlush(Errand.builder()
        .errander(member01)
        .title("두번째 의뢰 입니다.")
        .content("content")
        .images(List.of(ErrandImage.of("images/01")))
        .hashtags(new HashSet<>(List.of(ErrandHashtag.of("도배"), ErrandHashtag.of("알바"))))
        .pay(10000)
        .payDivision(PayDivision.HOURLY)
        .viewCount(1)
        .status(ErrandStatus.REQUEST)
        .build());

    em.getEntityManager().flush();
    em.getEntityManager().clear();
  }

  @Test
  @DisplayName("검색 조건 없는 의뢰 검색(search)에 성공합니다.")
  void search_with_no_condition_success() {
    ErrandSearchCondition cond = ErrandSearchCondition.builder()
        .build();
    int size = 10;
    int page = 0;
    PageRequest pageRequest = PageRequest.of(page, size);

    Slice<ErrandSearchResult> searchResult = errandQueryDslRepository.search(cond, pageRequest);

    assertThat(searchResult.getContent()).hasSize(2);
  }

  @Test
  @DisplayName("의뢰 제목으로 의뢰 검색(search)에 성공합니다.")
  void search_with_errand_title_success() {
    ErrandSearchCondition cond = ErrandSearchCondition.builder()
        .errandTitle("첫번째")
        .build();
    int size = 10;
    int page = 0;
    PageRequest pageRequest = PageRequest.of(page, size);

    Slice<ErrandSearchResult> searchResult = errandQueryDslRepository.search(cond, pageRequest);

    assertThat(searchResult.getContent()).hasSize(1);
  }

//  @Test
//  @DisplayName("의뢰 ID 리스트로 의뢰 ID에 매핑되는 의뢰 이미지를 조회합니다.")
//  void findImageUrlWithErrandId_ByErrandIds_success() {
//    List<Long> errandIds = List.of(1L, 2L);
//
//    Map<Long, List<ErrandImage>> imageMap = errandQueryDslRepository.findImageUrlWithErrandId(
//        errandIds);
//
//    assertThat(imageMap).hasSize(2);
//    assertThat(imageMap.get(1L)).hasSize(2);
//    assertThat(imageMap.get(2L)).hasSize(1);
//  }

  @Test
  @DisplayName("의뢰 ID 리스트로 의뢰 ID에 매핑되는 해쉬태그를 조회합니다.")
  void findHashtagsWithErrandId_ByErrandIds_success() {
    List<Long> errandIds = List.of(1L, 2L);

    Map<Long, List<ErrandHashtag>> hashtagMap = errandQueryDslRepository.findHashtagsWithErrandId(
        errandIds);

    assertThat(hashtagMap).hasSize(2);
    assertThat(hashtagMap.get(1L)).hasSize(2);
    assertThat(hashtagMap.get(2L)).hasSize(2);
  }
}
