package com.zerobase.nsbackend.global.fileUpload;

import com.zerobase.nsbackend.errand.domain.entity.ErrandImage;
import lombok.Getter;

@Getter
public class UploadFile {
  private String uploadFileName;
  private String storeFileName;

  public UploadFile(String uploadFileName, String storeFileName) {
    this.uploadFileName = uploadFileName;
    this.storeFileName = storeFileName;
  }

  public static UploadFile of(String uploadFileName, String storeFileName) {
    return new UploadFile(uploadFileName, storeFileName);
  }

  public ErrandImage toErrandImage() {
    return ErrandImage.of(storeFileName);
  }
}
