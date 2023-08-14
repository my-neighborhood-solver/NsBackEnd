package com.zerobase.nsbackend.errand.domain.repository;

import com.zerobase.nsbackend.errand.domain.entity.ErrandHashtag;
import com.zerobase.nsbackend.errand.domain.entity.ErrandImage;
import com.zerobase.nsbackend.errand.dto.ErrandSearchCondition;
import com.zerobase.nsbackend.errand.dto.search.ErrandSearchResult;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ErrandQueryDslRepository {
  Slice<ErrandSearchResult> search(ErrandSearchCondition condition, Pageable pageable);
  Map<Long, List<ErrandImage>> findImagesWithErrandId(List<Long> errandIds);
  Map<Long, List<ErrandHashtag>> findHashtagsWithErrandId(List<Long> errandIds);
}
