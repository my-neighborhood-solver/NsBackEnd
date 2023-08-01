package com.zerobase.nsbackend.global.fileUpload;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StoreFile {

  /**
   * 해당 파일을 반환합니다.
   * @param filename
   * @return
   */
  Resource getFile(String filename);

  /**
   * 하나의 파일을 업로드 합니다.
   * @param multipartFile
   * @return
   */
  UploadFile storeFile(MultipartFile multipartFile);

  /**
   * 파일을 삭제합니다.
   * @param filename
   */
  void deleteFile(String filename);
}
