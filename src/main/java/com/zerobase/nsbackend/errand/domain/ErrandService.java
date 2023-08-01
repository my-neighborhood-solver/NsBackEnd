package com.zerobase.nsbackend.errand.domain;

import static com.zerobase.nsbackend.global.exceptionHandle.ErrorCode.DONT_HAVE_AUTHORITY_TO_DELETE;
import static com.zerobase.nsbackend.global.exceptionHandle.ErrorCode.DONT_HAVE_AUTHORITY_TO_EDIT;

import com.zerobase.nsbackend.errand.domain.entity.Errand;
import com.zerobase.nsbackend.errand.domain.repository.ErrandRepository;
import com.zerobase.nsbackend.errand.dto.ErrandCreateRequest;
import com.zerobase.nsbackend.errand.domain.vo.ErrandStatus;
import com.zerobase.nsbackend.errand.dto.ErrandUpdateRequest;
import com.zerobase.nsbackend.global.auth.AuthManager;
import com.zerobase.nsbackend.global.exceptionHandle.ErrorCode;
import com.zerobase.nsbackend.member.domain.Member;
import com.zerobase.nsbackend.member.repository.MemberRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ErrandService {
  private final MemberRepository memberRepository;
  private final ErrandRepository errandRepository;
  private final AuthManager authManager;

  @Transactional
  public Errand createErrand(ErrandCreateRequest request) {
    Member member = getMemberFromAuth();

    return errandRepository.save(
        Errand.builder()
            .errander(member)
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
    if (!isErrander(errand.getErrander().getId())) {
      throw new IllegalStateException(DONT_HAVE_AUTHORITY_TO_EDIT.getDescription());
    }
    errand.edit(request.getTitle(), request.getContent(),
          request.getPayDivision(), request.getPay());
  }

  @Transactional
  public void cancelErrand(Long id) {
    Errand errand = getErrand(id);
    if (!isErrander(errand.getErrander().getId())) {
      throw new IllegalStateException(DONT_HAVE_AUTHORITY_TO_DELETE.getDescription());
    }
    errand.changeStatus(ErrandStatus.CANCEL);
  }

  private boolean isErrander(Long erranderId) {
    Member member = getMemberFromAuth();
    return Objects.equals(erranderId, member.getId());
  }

  private Member getMemberFromAuth() {
    String email = authManager.getUsername();
    return memberRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException(ErrorCode.MEMBER_NOT_FOUND.getDescription()));
  }
}
