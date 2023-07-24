package com.zerobase.nsbackend.errand.domain;

import com.zerobase.nsbackend.errand.domain.repository.ErrandRepository;
import com.zerobase.nsbackend.errand.dto.ErrandCreateRequest;
import com.zerobase.nsbackend.errand.domain.vo.ErrandStatus;
import com.zerobase.nsbackend.global.exceptionHandle.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ErrandService {
  private final ErrandRepository errandRepository;

  public Errand createErrand(ErrandCreateRequest request) {
    // TODO: 회원 유효성 체크 필요

    return errandRepository.save(
        Errand.builder()
            .title(request.getTitle())
            .content(request.getContent())
            .payDivision(request.getPayDivision())
            .pay(request.getPay())
            .status(ErrandStatus.REQUEST)
            .viewCount(0)
            .build());
  }
}
