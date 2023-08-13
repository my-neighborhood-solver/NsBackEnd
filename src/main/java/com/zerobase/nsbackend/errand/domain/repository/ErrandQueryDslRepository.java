package com.zerobase.nsbackend.errand.domain.repository;

import com.zerobase.nsbackend.errand.dto.ErrandSearchCondition;
import com.zerobase.nsbackend.errand.dto.ErrandSearchResult;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ErrandQueryDslRepository {
  Slice<ErrandSearchResult> search(ErrandSearchCondition condition, Pageable pageable);
}
