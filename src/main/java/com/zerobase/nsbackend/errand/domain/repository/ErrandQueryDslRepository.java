package com.zerobase.nsbackend.errand.domain.repository;

import com.zerobase.nsbackend.errand.dto.ErrandSearchCondition;
import com.zerobase.nsbackend.errand.dto.ErrandSearchResult;
import java.util.List;

public interface ErrandQueryDslRepository {
  List<ErrandSearchResult> search(ErrandSearchCondition condition);
}
