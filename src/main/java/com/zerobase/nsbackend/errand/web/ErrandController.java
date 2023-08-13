package com.zerobase.nsbackend.errand.web;

import com.zerobase.nsbackend.errand.domain.entity.Errand;
import com.zerobase.nsbackend.errand.domain.ErrandService;
import com.zerobase.nsbackend.errand.dto.ErrandChangAddressRequest;
import com.zerobase.nsbackend.errand.dto.ErrandCreateRequest;
import com.zerobase.nsbackend.errand.dto.ErrandDto;
import com.zerobase.nsbackend.errand.dto.ErrandSearchCondition;
import com.zerobase.nsbackend.errand.dto.ErrandSearchResult;
import com.zerobase.nsbackend.errand.dto.ErrandUpdateRequest;
import com.zerobase.nsbackend.errand.dto.ErranderDto;
import com.zerobase.nsbackend.errand.dto.LikeErrandResponse;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
@RequestMapping("/errands")
@RestController
public class ErrandController {

  private final ErrandService errandService;

  @PostMapping
  public ResponseEntity<Void> createErrand(
      @RequestPart("images") @Nullable List<MultipartFile> images,
      @RequestPart("errand") ErrandCreateRequest request) {
    Errand errand = errandService.createErrand(request, images);
    return ResponseEntity.created(URI.create("/errands/" + errand.getId())).build();
  }

  @GetMapping
  public ResponseEntity<List<ErrandDto>> readAllErrand() {
    return ResponseEntity.ok(errandService.getAllErrands());
  }

  @GetMapping("/{id}")
  public ResponseEntity<ErrandDto> readErrand(@PathVariable Long id) {
    return ResponseEntity.ok(errandService.getErrandDto(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<Void> updateErrand(@RequestBody ErrandUpdateRequest request
      , @PathVariable Long id) {
    errandService.editErrand(request, id);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/{id}/cancel")
  public ResponseEntity<Void> cancelErrand(@PathVariable Long id) {
    errandService.cancelErrand(id);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/{id}/hashtag")
  public ResponseEntity<Void> addHashtag(@PathVariable Long id, @RequestParam String tag) {
    errandService.addHashtag(id, tag);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{id}/hashtag")
  public ResponseEntity<Void> deleteHashtag(@PathVariable Long id, @RequestParam String tag) {
    errandService.deleteHashtag(id, tag);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/{id}/address")
  public ResponseEntity<Void> changeErrandAddress(
      @PathVariable Long id,
      @RequestBody ErrandChangAddressRequest request) {
    errandService.changeAddress(id, request);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/{id}/like")
  public ResponseEntity<LikeErrandResponse> likeErrand(@PathVariable Long id) {
    return ResponseEntity.ok(LikeErrandResponse.of(errandService.likeErrand(id)));
  }

  @GetMapping("/{id}/errander")
  public ResponseEntity<ErranderDto> readErrander(@PathVariable Long id) {
    return ResponseEntity.ok(errandService.findErrander(id));
  }

  /**
   * 의뢰를 검색합니다.
   * @param condition
   * @param
   * @return
   */
  @GetMapping("/search")
  public ResponseEntity<Slice<ErrandSearchResult>> searchErrand(
      @RequestBody ErrandSearchCondition condition,
      Pageable pageable) {
    return ResponseEntity.ok(errandService.searchErrand(condition, pageable));
  }
}
