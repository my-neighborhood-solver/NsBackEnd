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

  @Override
  public Resource getFile(String filename) {
    S3Object s3Object = s3Client.getObject(bucketName, filename);
    S3ObjectInputStream inputStream = s3Object.getObjectContent();
    return new InputStreamResource(inputStream);
  }

  @Override
  public UploadFile storeFile(MultipartFile multipartFile) {
    log.info("### bucketName : " + bucketName);

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
  public void deleteFile(String filename) {
    s3Client.deleteObject(bucketName, filename);
  }
}
