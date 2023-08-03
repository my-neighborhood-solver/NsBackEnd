package com.zerobase.nsbackend.global.fileUpload;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.zerobase.nsbackend.global.customException.FileUploadException;
import com.zerobase.nsbackend.global.exceptionHandle.ErrorCode;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
public class StoreFileToAWS implements StoreFile {

  @Value("${application.bucket.name}")
  private String bucketName;

  private final AmazonS3 s3Client;

  /**
   * 파일 이름으로 파일을 조회합니다.
   * @param filename
   * @return Resource 타입의 파일
   */
  @Override
  public Resource getFile(String filename) {
    S3Object s3Object = s3Client.getObject(bucketName, filename);
    S3ObjectInputStream inputStream = s3Object.getObjectContent();
    return new InputStreamResource(inputStream);
  }

  /**
   * 하나의 파일을 AWS S3에 저장합니다.
   * @param multipartFile
   * @return 저장된 파일 이름(UUID), 원래 파일 이름
   */
  @Override
  public UploadFile storeFile(MultipartFile multipartFile) {
    String storeFileName = createStoreFileName(multipartFile.getOriginalFilename());
    ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentType(multipartFile.getContentType());

    try (InputStream inputStream = multipartFile.getInputStream()) {
      s3Client.putObject(new PutObjectRequest(bucketName, storeFileName, inputStream, objectMetadata));
    } catch (IOException exception) {
      throw new FileUploadException(ErrorCode.FAIL_TO_UPLOAD_FILE.getDescription(), exception);
    }

    return UploadFile.of(multipartFile.getOriginalFilename(), storeFileName);
  }

  @Override
  public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) {
    if (multipartFiles == null || multipartFiles.isEmpty()) {
      return new ArrayList<>();
    }

    List<UploadFile> uploadFiles = new ArrayList<>();
    for (MultipartFile file: multipartFiles) {
      uploadFiles.add(storeFile(file));
    }
    return uploadFiles;
  }

  /**
   * 파일 이름으로 파일을 삭제합니다.
   * @param filename
   */
  @Override
  public void deleteFile(String filename) {
    s3Client.deleteObject(bucketName, filename);
  }

  /**
   * 저장할 파일 이름을 생성합니다.
   * UUID 에 기존 파일의 확장자를 붙혀서 만듭니다.
   * @param originalFilename
   * @return
   */
  private String createStoreFileName(String originalFilename) {
    String ext = extractExt(originalFilename);
    String uuid = UUID.randomUUID().toString();
    return uuid + "." + ext;
  }

  /**
   * 파일의 확장자를 반환합니다.
   * @param originalFilename
   * @return
   */
  private String extractExt(String originalFilename) {
    int pos = originalFilename.lastIndexOf(".");
    return originalFilename.substring(pos + 1);
  }
}
