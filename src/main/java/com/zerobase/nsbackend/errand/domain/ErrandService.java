package com.zerobase.nsbackend.errand.domain;

import static com.zerobase.nsbackend.global.exceptionHandle.ErrorCode.DONT_HAVE_AUTHORITY_TO_DELETE;
import static com.zerobase.nsbackend.global.exceptionHandle.ErrorCode.DONT_HAVE_AUTHORITY_TO_EDIT;

import com.zerobase.nsbackend.errand.domain.entity.Errand;
import com.zerobase.nsbackend.errand.domain.entity.ErrandImage;
import com.zerobase.nsbackend.errand.domain.repository.ErrandRepository;
import com.zerobase.nsbackend.errand.dto.ErrandCreateRequest;
import com.zerobase.nsbackend.errand.domain.vo.ErrandStatus;
import com.zerobase.nsbackend.errand.dto.ErrandUpdateRequest;
import com.zerobase.nsbackend.global.auth.AuthManager;
import com.zerobase.nsbackend.global.exceptionHandle.ErrorCode;
import com.zerobase.nsbackend.global.fileUpload.StoreFile;
import com.zerobase.nsbackend.global.fileUpload.UploadFile;
import com.zerobase.nsbackend.member.domain.Member;
import com.zerobase.nsbackend.member.repository.MemberRepository;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class ErrandService {
  private final MemberRepository memberRepository;
  private final ErrandRepository errandRepository;
  private final AuthManager authManager;
  private final StoreFile storeFile;

  @Transactional
  public Errand createErrand(ErrandCreateRequest request, List<MultipartFile> imageRequest) {
    // Security Context로 부터 인증된 유저 정보 받아오기
    Member member = getMemberFromAuth();

    // 아미지 업로드
    List<UploadFile> uploadFiles = storeFile.storeFiles(imageRequest);
    List<ErrandImage> images = uploadFiles.stream().map(UploadFile::toErrandImage)
        .collect(Collectors.toList());

    return errandRepository.save(
        Errand.builder()
            .errander(member)
            .title(request.getTitle())
            .content(request.getContent())
            .images(images)
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

  @Transactional
  public void addHashtag(Long id, String tag) {
    Errand errand = getErrand(id);
    errand.addHashtag(tag);
  }

  @Transactional
  public void deleteHashtag(Long id, String tag) {
    Errand errand = getErrand(id);
    errand.removeHashtag(tag);
  }
}
