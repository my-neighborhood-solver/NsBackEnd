package com.zerobase.nsbackend.errand.domain;

import com.zerobase.nsbackend.errand.dto.ErrandCreateRequest;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/errands")
@RestController
public class ErrandController {
  private final ErrandService errandService;

  @PostMapping
  public ResponseEntity<Void> createErrand(@RequestBody ErrandCreateRequest request) {
    Errand errand = errandService.createErrand(request);
    return ResponseEntity.created(URI.create("/errands/" + errand.getId())).build();
  }
}
