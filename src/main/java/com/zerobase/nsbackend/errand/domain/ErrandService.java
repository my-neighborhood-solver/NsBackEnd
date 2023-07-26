package com.zerobase.nsbackend.errand.domain;

import com.zerobase.nsbackend.errand.domain.repository.ErrandRepository;
import com.zerobase.nsbackend.errand.dto.ErrandCreateRequest;
import com.zerobase.nsbackend.errand.domain.vo.ErrandStatus;
import com.zerobase.nsbackend.errand.dto.ErrandUpdateRequest;
import com.zerobase.nsbackend.global.exceptionHandle.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ErrandService {
  private final ErrandRepository errandRepository;

  @Transactional
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

  public Errand getErrand(Long id) {
    return errandRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException(ErrorCode.ERRAND_NOT_FOUND.getDescription()));
  }

  @Transactional
  public void editErrand(ErrandUpdateRequest request, Long id) {
    Errand errand = getErrand(id);
    errand.edit(request.getTitle(), request.getContent()
        , request.getPayDivision(), request.getPay());
  }

  @Transactional
  public void cancelErrand(Long id) {
    //TODO: 인증, 인가 도입 후 의뢰자만 삭제 가능하도록 수정

    Errand errand = getErrand(id);
    errand.changeStatus(ErrandStatus.CANCEL);
  }
}
