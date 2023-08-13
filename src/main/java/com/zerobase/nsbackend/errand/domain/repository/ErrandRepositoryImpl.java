package com.zerobase.nsbackend.errand.domain.repository;

import static com.querydsl.jpa.JPAExpressions.selectFrom;
import static com.zerobase.nsbackend.errand.domain.entity.QErrand.*;
import static com.zerobase.nsbackend.errand.domain.entity.QErrandHashtag.*;
import static com.zerobase.nsbackend.errand.domain.entity.QErrandImage.*;
import static com.zerobase.nsbackend.errand.domain.entity.QLikedMember.*;
import static com.zerobase.nsbackend.member.domain.QMember.*;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.nsbackend.errand.domain.entity.Errand;
import com.zerobase.nsbackend.errand.domain.entity.ErrandHashtag;
import com.zerobase.nsbackend.errand.domain.entity.QErrand;
import com.zerobase.nsbackend.errand.domain.entity.QErrandHashtag;
import com.zerobase.nsbackend.errand.domain.entity.QErrandImage;
import com.zerobase.nsbackend.errand.domain.entity.QLikedMember;
import com.zerobase.nsbackend.errand.domain.repository.ErrandQueryDslRepository;
import com.zerobase.nsbackend.errand.dto.ErrandSearchCondition;
import com.zerobase.nsbackend.errand.dto.ErrandSearchResult;
import com.zerobase.nsbackend.errand.dto.QErrandSearchResult;
import com.zerobase.nsbackend.member.domain.Member;
import com.zerobase.nsbackend.member.domain.QMember;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class ErrandRepositoryImpl implements ErrandQueryDslRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<ErrandSearchResult> search(ErrandSearchCondition condition) {
    return queryFactory
        .select(new QErrandSearchResult(
            errand.id,
            errand.title,
            errand.content,
            likedMember.count(),
            errand.viewCount,
            Expressions.stringTemplate("DATE_FORMAT({0}, {1})"
                , errand.createdAt
                , ConstantImpl.create("%Y-%m-%d")),
            member.nickname,
            member.profileImage
            )
        ).distinct()
        .from(errand)
          .join(errand.errander, member)
          .leftJoin(errand.images, errandImage)
          .leftJoin(errand.hashtags, errandHashtag)
          .leftJoin(errand.likedMembers, likedMember)
        .where(
          errandTitleContains(condition.getErrandTitle()),
          errandHashtagsEq(condition.getHashtag())
        )
        .groupBy(errand.id, errand.title, errand.content, errand.viewCount,
            errand.createdAt, member.nickname, member.profileImage)
        .fetch();
  }

  private BooleanExpression errandTitleContains(String errandTitle) {
    return StringUtils.hasText(errandTitle) ? errand.title.contains(errandTitle) : null;
  }
  private BooleanExpression errandHashtagsEq(String hashtag) {
    return hashtag != null ? errand.hashtags.contains(ErrandHashtag.of(hashtag)) : null;
  }
}
