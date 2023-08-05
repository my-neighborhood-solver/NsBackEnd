package com.zerobase.nsbackend.errand.web;

import com.zerobase.nsbackend.errand.domain.entity.Errand;
import com.zerobase.nsbackend.errand.domain.ErrandService;
import com.zerobase.nsbackend.errand.dto.ErrandCreateRequest;
import com.zerobase.nsbackend.errand.dto.ErrandDto;
import com.zerobase.nsbackend.errand.dto.ErrandUpdateRequest;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
  public ResponseEntity<Void> createErrand(@RequestPart("images") @Nullable List<MultipartFile> images,
      @RequestPart("errand") ErrandCreateRequest request) {
    Errand errand = errandService.createErrand(request, images);
    return ResponseEntity.created(URI.create("/errands/" + errand.getId())).build();
  }

  @GetMapping("/{id}")
  public ResponseEntity<ErrandDto> readErrand(@PathVariable Long id) {
    return ResponseEntity.ok(ErrandDto.from(errandService.getErrand(id)));
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
}
