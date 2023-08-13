package com.zerobase.nsbackend.errand.domain.repository;

import static com.zerobase.nsbackend.errand.domain.entity.QErrand.*;
import static com.zerobase.nsbackend.errand.domain.entity.QLikedMember.*;
import static com.zerobase.nsbackend.member.domain.QMember.*;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.nsbackend.errand.domain.entity.ErrandHashtag;
import com.zerobase.nsbackend.errand.dto.ErrandSearchCondition;
import com.zerobase.nsbackend.errand.dto.ErrandSearchResult;
import com.zerobase.nsbackend.errand.dto.QErrandSearchResult;
import java.time.LocalDateTime;
import java.util.List;
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
    List<ErrandSearchResult> data = queryFactory
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
    return PageableExecutionUtils.getPage(data, pageable, data::size);
  }

  private StringTemplate convertDateFormat(DateTimePath<LocalDateTime> date) {
    return Expressions.stringTemplate("DATE_FORMAT({0}, {1})"
        , date
        , ConstantImpl.create("%Y-%m-%d"));
  }

  private BooleanExpression errandTitleContains(String errandTitle) {
    return StringUtils.hasText(errandTitle) ? errand.title.contains(errandTitle) : null;
  }
}
