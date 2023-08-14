package com.zerobase.nsbackend.errand.domain.repository;

import static com.zerobase.nsbackend.errand.domain.entity.QErrand.*;
import static com.zerobase.nsbackend.errand.domain.entity.QErrandHashtag.*;
import static com.zerobase.nsbackend.errand.domain.entity.QErrandImage.*;
import static com.zerobase.nsbackend.errand.domain.entity.QLikedMember.*;
import static com.zerobase.nsbackend.member.domain.QMember.*;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.nsbackend.errand.domain.entity.ErrandHashtag;
import com.zerobase.nsbackend.errand.domain.entity.ErrandImage;
import com.zerobase.nsbackend.errand.dto.ErrandSearchCondition;
import com.zerobase.nsbackend.errand.dto.search.ErrandSearchResult;
import com.zerobase.nsbackend.errand.dto.search.QErrandSearchResult;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class ErrandRepositoryImpl implements ErrandQueryDslRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public Slice<ErrandSearchResult> search(ErrandSearchCondition condition, Pageable pageable) {
    // 의뢰 검색 기본 데이터 조회
    List<ErrandSearchResult> results = searchBaseErrandData(condition, pageable);
    // 조회한 의뢰ID 추출
    List<Long> errandIds = getErrandIds(results);

    // 아미지 조회, 해쉬태그 조회
    Map<Long, List<ErrandImage>> imageMap = findImagesWithErrandId(errandIds);
    Map<Long, List<ErrandHashtag>> hashtagMap = findHashtagsWithErrandId(errandIds);

    for (ErrandSearchResult result: results) {
      Long errandId = result.getErrandId();
      // 이미지 세팅
      if (isImageExists(imageMap, errandId)) {
        ErrandImage firstImage = imageMap.get(errandId).get(0);
        result.changeImageUrl(firstImage.getFullImageUrl());
      }
      // 해쉬태그 세팅
      if (hashtagMap.containsKey(errandId)) {
        List<String> hashtags = hashtagMap.get(errandId)
            .stream().map(ErrandHashtag::getName).collect(Collectors.toList());
        result.changeHashtags(hashtags);
      }
    }

    return PageableExecutionUtils.getPage(results, pageable, results::size);
  }

  private boolean isImageExists(Map<Long, List<ErrandImage>> imageMap, Long errandId) {
    return imageMap.containsKey(errandId) && !imageMap.get(errandId).isEmpty();
  }

  private List<Long> getErrandIds(List<ErrandSearchResult> results) {
    return results.stream().map(ErrandSearchResult::getErrandId)
        .collect(Collectors.toList());
  }

  /**
   * 의뢰 기본 데이터를 조회합니다.
   * @param condition
   * @param pageable
   * @return
   */
  private List<ErrandSearchResult> searchBaseErrandData(ErrandSearchCondition condition,
      Pageable pageable) {
    return queryFactory
        .select(new QErrandSearchResult(
                errand.id,
                errand.title,
                errand.content,
                likedMember.count(),
                errand.viewCount,
                convertDateFormat(errand.createdAt),
                member.nickname,
                member.profileImage
            )
        ).distinct()
        .from(errand)
        .join(errand.errander, member)
        .leftJoin(errand.likedMembers, likedMember)
        .where(
            errandTitleContains(condition.getErrandTitle())
        )
        .groupBy(errand.id, errand.title, errand.content, errand.viewCount,
            errand.createdAt, member.nickname, member.profileImage)
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();
  }

  private StringTemplate convertDateFormat(DateTimePath<LocalDateTime> date) {
    return Expressions.stringTemplate("DATE_FORMAT({0}, {1})"
        , date
        , ConstantImpl.create("%Y-%m-%d"));
  }

  private BooleanExpression errandTitleContains(String errandTitle) {
    return StringUtils.hasText(errandTitle) ? errand.title.contains(errandTitle) : null;
  }

  /**
   * 의뢰 ID 리스트로 의뢰 ID에 매핑되는 이미지를 조회합니다.
   * @param errandIds
   * @return
   */
  @Override
  public Map<Long, List<ErrandImage>> findImagesWithErrandId(List<Long> errandIds) {
    return queryFactory
        .from(errand)
        .leftJoin(errand.images ,errandImage)
        .where(errand.id.in(errandIds))
        .transform(GroupBy.groupBy(errand.id).as(GroupBy.list(errandImage)));
  }

  /**
   * 의뢰 ID 리스트로 의뢰 ID에 매핑되는 해쉬태그를 조회합니다.
   * @param errandIds
   * @return
   */
  @Override
  public Map<Long, List<ErrandHashtag>> findHashtagsWithErrandId(List<Long> errandIds) {
    return queryFactory
        .from(errand)
        .leftJoin(errand.hashtags ,errandHashtag)
        .where(errand.id.in(errandIds))
        .transform(GroupBy.groupBy(errand.id).as(GroupBy.list(errandHashtag)));
  }
}
