package com.zerobase.nsbackend.errand.domain.service;

import static com.zerobase.nsbackend.global.exceptionHandle.ErrorCode.CANNOT_CHOOSE_PERFORMER_WHEN_FINISHED;
import static com.zerobase.nsbackend.global.exceptionHandle.ErrorCode.CAN_FINISH_ONLY_PERFORMING;
import static com.zerobase.nsbackend.global.exceptionHandle.ErrorCode.DONT_HAVE_AUTHORITY;
import static com.zerobase.nsbackend.global.exceptionHandle.ErrorCode.INVALID_INPUT_ERROR;
import static com.zerobase.nsbackend.global.exceptionHandle.ErrorCode.PERFORMER_ALREADY_EXISTS;
import static com.zerobase.nsbackend.global.exceptionHandle.ErrorCode.REVIEWEE_IS_UNVALID;
import static com.zerobase.nsbackend.global.exceptionHandle.ErrorCode.REVIEW_ONLY_CAN_FINISH;
import static com.zerobase.nsbackend.global.exceptionHandle.ErrorCode.REVIEW_ONLY_CAN_PERFORMER;

import com.zerobase.nsbackend.errand.domain.entity.Errand;
import com.zerobase.nsbackend.errand.domain.entity.ErrandHashtag;
import com.zerobase.nsbackend.errand.domain.entity.ErrandImage;
import com.zerobase.nsbackend.errand.domain.entity.Performer;
import com.zerobase.nsbackend.errand.domain.entity.Review;
import com.zerobase.nsbackend.errand.domain.repository.ErrandRepository;
import com.zerobase.nsbackend.errand.domain.repository.PerformerRepository;
import com.zerobase.nsbackend.errand.domain.repository.ReviewRepository;
import com.zerobase.nsbackend.errand.domain.vo.ReviewDivision;
import com.zerobase.nsbackend.errand.dto.ErrandChangAddressRequest;
import com.zerobase.nsbackend.errand.dto.ErrandCreateRequest;
import com.zerobase.nsbackend.errand.domain.vo.ErrandStatus;
import com.zerobase.nsbackend.errand.dto.ErrandDto;
import com.zerobase.nsbackend.errand.dto.ErrandSearchCondition;
import com.zerobase.nsbackend.errand.dto.ReviewErrandRequest;
import com.zerobase.nsbackend.errand.dto.search.ErrandSearchResult;
import com.zerobase.nsbackend.errand.dto.ErrandUpdateRequest;
import com.zerobase.nsbackend.errand.dto.ErranderDto;
import com.zerobase.nsbackend.global.auth.AuthManager;
import com.zerobase.nsbackend.global.exceptionHandle.ErrorCode;
import com.zerobase.nsbackend.global.fileUpload.StoreFile;
import com.zerobase.nsbackend.global.fileUpload.UploadFile;
import com.zerobase.nsbackend.member.domain.Member;
import com.zerobase.nsbackend.member.repository.MemberRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class ErrandService {
  private final MemberRepository memberRepository;
  private final ErrandRepository errandRepository;
  private final PerformerRepository performerRepository;
  private final ReviewRepository reviewRepository;
  private final AuthManager authManager;
  private final StoreFile storeFile;

  @Transactional
  public Errand createErrand(ErrandCreateRequest request, List<MultipartFile> imageRequest) {
    Member member = getMemberFromAuth();

    // 아미지 업로드
    List<UploadFile> uploadFiles = storeFile.storeFiles(imageRequest);
    List<ErrandImage> images = uploadFiles.stream()
        .map(UploadFile::toErrandImage)
        .collect(Collectors.toList());

    // 해쉬태그
    Set<ErrandHashtag> hashtagSet = request.getHashtags().stream()
        .map(ErrandHashtag::of)
        .collect(Collectors.toSet());

    return errandRepository.save(
        Errand.builder()
            .errander(member)
            .title(request.getTitle())
            .content(request.getContent())
            .images(images)
            .payDivision(request.getPayDivision())
            .pay(request.getPay())
            .deadLine(convertToLocalDateTime(request.getDeadLine()))
            .hashtags(hashtagSet)
            .status(ErrandStatus.REQUEST)
            .viewCount(0)
            .build());
  }

  private LocalDate convertToLocalDateTime(Date date) {
    if (date == null) {
      return null;
    }
    return LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
  }

  public Errand getErrand(Long id) {
    Errand errand = errandRepository.findWithFetchById(id)
        .orElseThrow(
            () -> new IllegalArgumentException(ErrorCode.ERRAND_NOT_FOUND.getDescription()));
    errand.increaseViewCount();
    return errand;
  }

  @Transactional
  public ErrandDto getErrandDto(Long id) {
    Errand errand = getErrand(id);
    Member memberFromAuth = getMemberFromAuth();

    boolean isLiked = errand.checkLiked(memberFromAuth);
    return ErrandDto.from(errand, isLiked);
  }

  @Transactional
  public void editErrand(ErrandUpdateRequest request, Long id) {
    Errand errand = getErrand(id);

    validateIsErrander(errand.getId());
    errand.edit(request.getTitle(), request.getContent(),
          request.getPayDivision(), request.getPay());
  }

  @Transactional
  public void cancelErrand(Long id) {
    Errand errand = getErrand(id);

    validateIsErrander(errand.getId());
    errand.changeStatus(ErrandStatus.CANCEL);
  }

  /**
   * 현재 로그인한 사용자가 의뢰자인지 유효성 체크합니다.
   * @param erranderId
   */
  private void validateIsErrander(Long erranderId) {
    Member member = getMemberFromAuth();
    if(!Objects.equals(erranderId, member.getId())) {
      throw new IllegalStateException(DONT_HAVE_AUTHORITY.getDescription());
    }
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

  @Transactional
  public List<ErrandDto> getAllErrands() {
    Member member = getMemberFromAuth();
    return errandRepository.findWithFetchAll()
        .stream().map(errand -> ErrandDto.from(errand, errand.checkLiked(member)))
        .collect(Collectors.toList());
  }

  @Transactional
  public void changeAddress(Long id, ErrandChangAddressRequest request) {
    Errand errand = getErrand(id);
    errand.changeAddress(request.toAddress());
  }

  /**
   * 좋아요 처리를 합니다.
   * @param id
   * @return 로그인한 회원의 해당 의뢰에 대한 좋아요 여부
   */
  @Transactional
  public boolean likeErrand(Long id) {
    Member member = getMemberFromAuth();
    Errand errand = getErrand(id);
    errand.like(member);

    return errand.checkLiked(member);
  }

  public ErranderDto findErrander(Long errandId) {
    Errand errand = getErrand(errandId);
    Member errander = getErrander(errand);

    Integer errandCount = errandRepository.countByErranderId(errander.getId());
    return ErranderDto.of(errander, errandCount);
  }

  private Member getErrander(Errand errand) {
    return getMemberById(errand.getErrander().getId());
  }

  private Member getMemberById(Long id) {
    return memberRepository.findById(id)
        .orElseThrow(() -> new IllegalStateException(ErrorCode.MEMBER_NOT_FOUND.getDescription()));
  }

  @Transactional
  public Slice<ErrandSearchResult> searchErrand(ErrandSearchCondition condition, Pageable pageable) {
    return errandRepository.search(condition, pageable);
  }

  /**
   * 의뢰 마감
   * @param errandId
   */
  @Transactional
  public void finishErrand(Long errandId) {
    Errand errand = getErrand(errandId);
    validateFinishErrand(errand);
    errand.finish();
  }

  private void validateFinishErrand(Errand errand) {
    validateIsErrander(errand.getId());
    if (errand.getStatus() != ErrandStatus.PERFORMING) {
      throw new IllegalStateException(CAN_FINISH_ONLY_PERFORMING.getDescription());
    }
  }

  /**
   * 의뢰의 수행자를 선택합니다.
   * @param errandId 의뢰 ID
   * @param performerId 수행자 ID
   */
  @Transactional
  public void choosePerformer(Long errandId, Long performerId) {
    Errand errand = getErrand(errandId);
    Member member = getMemberById(performerId);

    validateChoosePerformer(errand, member);

    performerRepository.save(Performer.of(errand, member));
    errand.performing();
  }

  private void validateChoosePerformer(Errand errand, Member member) {
    if (isErrandFinished(errand)) {
      throw new IllegalStateException(CANNOT_CHOOSE_PERFORMER_WHEN_FINISHED.getDescription());
    }

    boolean memberExists = performerRepository.existsByErrandAndMember(errand, member);
    if (memberExists) {
      throw new IllegalArgumentException(PERFORMER_ALREADY_EXISTS.getDescription());
    }
  }

  private static boolean isErrandFinished(Errand errand) {
    return errand.getStatus() == ErrandStatus.FINISH || errand.getStatus() == ErrandStatus.CANCEL;
  }

  /**
   * 의뢰 리뷰를 등록합니다.
   * @param errandId
   * @param request
   */
  public void reviewErrand(Long errandId, ReviewErrandRequest request) {
    Errand errand = getErrand(errandId);
    if (!errand.isFinished()) {
      throw new IllegalStateException(REVIEW_ONLY_CAN_FINISH.getDescription());
    }
    Member performer = getPerformer(request, errand);

    validateReviewErrand(errand, performer);

    reviewRepository.save(Review.of(errand, performer, request.getReviewGrade(),
        request.getComment(), request.getDivision()));
  }

  private Member getPerformer(ReviewErrandRequest request, Errand errand) {
    // 의뢰자 리뷰의 경우
    if (request.getDivision() == ReviewDivision.ERRANDER_REVIEW) {
      validRevieweeIsErrander(request, errand);
      return getMemberFromAuth();
    }
    // 수행자 리뷰의 경우
    if (request.getDivision() == ReviewDivision.PERFORMER_REVIEW) {
      Long revieweeId = request.getRevieweeId();
      return getMemberById(revieweeId);
    }
    throw new IllegalArgumentException(INVALID_INPUT_ERROR.getDescription());
  }

  /**
   * 피평가자가 리뷰의 의뢰자인지 체크합니다.
   */
  private void validRevieweeIsErrander(ReviewErrandRequest request, Errand errand) {
    if(!Objects.equals(request.getRevieweeId(), errand.getErrander().getId())) {
      throw new IllegalArgumentException(REVIEWEE_IS_UNVALID.getDescription());
    }
  }

  private void validateReviewErrand(Errand errand, Member performer) {
    boolean isExists = performerRepository.existsByErrandAndMember(errand, performer);
    if (!isExists) {
      throw new IllegalArgumentException(REVIEW_ONLY_CAN_PERFORMER.getDescription());
    }
  }
}
