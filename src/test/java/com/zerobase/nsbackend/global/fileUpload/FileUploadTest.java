package com.zerobase.nsbackend.global.fileUpload;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.zerobase.nsbackend.integrationTest.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

class FileUploadTest extends IntegrationTest {
  @WithMockUser("USER")
  @Test
  @DisplayName("파일 업르드 테스트")
  public void whenFileUploaded_thenVerifyStatus()
      throws Exception {
    // given
    MockMultipartFile file
        = new MockMultipartFile(
        "file",
        "hello.txt",
        MediaType.TEXT_PLAIN_VALUE,
        "Hello, World!".getBytes()
    );

    // when
    ResultActions resultActions = mvc.perform(multipart("/images").file(file))
        .andDo(print());

    // then
    resultActions.andExpect(status().isCreated());

    // 테스트로 생성된 데이터 삭제
    removeFileAfterTest(resultActions);
  }

  @WithMockUser("USER")
  @Test
  @DisplayName("파일 조회 테스트")
  void getFile_success() throws Exception {
    // given
    MockMultipartFile file
        = new MockMultipartFile(
        "file",
        "hello.txt",
        MediaType.TEXT_PLAIN_VALUE,
        "Hello, World!".getBytes()
    );
    ResultActions createAction = mvc.perform(multipart("/images").file(file));
    String newFileName = getNewFileName(createAction);

    // when
    ResultActions resultActions = mvc.perform(get("/images/{filename}", newFileName))
        .andDo(print());

    // then
    resultActions.andExpect(status().isOk());

    // 테스트 데이터 삭제
    removeFileAfterTest(createAction);
  }

  /**
   * 저장된 파일 이름을 반환합니다.
   * @param resultActions
   * @return
   * @throws Exception
   */
  private String getNewFileName(ResultActions resultActions) throws Exception {
    String bodyString = resultActions.andReturn().getResponse().getContentAsString();
    return objectMapper.readValue(bodyString, UploadFile.class).getStoreFileName();
  }

  /**
   * 테스트로 생성된 데이터를 삭제합니다.
   * @param resultActions
   * @throws Exception
   */
  private void removeFileAfterTest(ResultActions resultActions) throws Exception {
    String newFileName = getNewFileName(resultActions);
    mvc.perform(delete("/images/{filename}", newFileName));
  }
}
