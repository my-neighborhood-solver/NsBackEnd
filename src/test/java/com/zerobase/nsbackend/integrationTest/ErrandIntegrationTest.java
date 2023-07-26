package com.zerobase.nsbackend.integrationTest;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.zerobase.nsbackend.errand.domain.vo.PayDivision;
import com.zerobase.nsbackend.errand.dto.ErrandCreateRequest;
import com.zerobase.nsbackend.errand.dto.ErrandUpdateRequest;
import com.zerobase.nsbackend.global.exceptionHandle.ErrorCode;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("의뢰 통합 테스트")
public class ErrandIntegrationTest extends IntegrationTest {

  @Test
  @DisplayName("의뢰 생성에 성공합니다.")
  void createErrand_success() throws Exception {
    // given
    ErrandCreateRequest createRequest = ErrandCreateRequest.builder()
        .title("testTitle")
        .content("testContent")
        .payDivision(PayDivision.HOURLY)
        .pay(10000)
        .build();

    // when
    ResultActions resultActions = mvc.perform(
        post("/errands")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(createRequest))
            .accept(MediaType.APPLICATION_JSON));

    // then
    resultActions
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", CoreMatchers.startsWith("/errands/")));
  }

  @Test
  @DisplayName("의뢰 ID로 의뢰 조회에 성공합니다.")
  void readErrandById_success() throws Exception {
    // given
    String title = "testTitle";
    String content = "testContent";
    PayDivision payDivision = PayDivision.HOURLY;
    Integer pay = 10000;
    Long errandId = createErrandForGiven(title, content, payDivision, pay);

    // when
    ResultActions resultActions = requestGetErrand(errandId);

    // then
    resultActions
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(errandId))
        .andExpect(jsonPath("$.title").value(is(title)))
        .andExpect(jsonPath("$.content").value(is(content)))
        .andExpect(jsonPath("$.payDivision").value(is(payDivision.name())))
        .andExpect(jsonPath("$.pay").value(is(pay)));
  }

  @Test
  @DisplayName("의뢰ID로 의뢰 조회 시, 의뢰 ID가 없어서 실패합니다.")
  void readErrandById_fail_because_errand_not_found() throws Exception {
    // given
    String title = "testTitle";
    String content = "testContent";
    PayDivision payDivision = PayDivision.HOURLY;
    Integer pay = 10000;
    Long errandId = createErrandForGiven(title, content, payDivision, pay);

    Long notFoundId = 100L;

    // when
    ResultActions resultActions = requestGetErrand(notFoundId);

    // then
    resultActions
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("errorCode").value(is(ErrorCode.ERRAND_NOT_FOUND.getCode())));
  }

  @Test
  @DisplayName("의뢰 성공적으로 수정합니다.")
  void updateErrand_success() throws Exception {
    // given
    Long errandId = createErrandForGiven("testTitle", "testContent", PayDivision.HOURLY, 10000);

    ErrandUpdateRequest updateRequest = ErrandUpdateRequest.builder()
        .title("updatedTitle")
        .content("updatedContent")
        .payDivision(PayDivision.UNIT)
        .pay(10000)
        .build();

    // when
    ResultActions resultActions = mvc.perform(
        put("/errands/{id}", errandId)
            .content(asJsonString(updateRequest))
            .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    resultActions
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("의뢰 취소에 성공합니다.")
  void cancelErrand_success() throws Exception {
    Long errandId = createErrandForGiven("testTitle", "testContent", PayDivision.UNIT, 1000);

    ResultActions perform = mvc.perform(
        post("/errands/{id}/cancel", errandId));

    perform.andExpect(status().isOk());
  }

  private ResultActions requestCreateErrand(String title, String content, PayDivision payDivision, Integer pay)
      throws Exception {
    ErrandCreateRequest createRequest = ErrandCreateRequest.builder()
        .title(title)
        .content(content)
        .payDivision(payDivision)
        .pay(pay)
        .build();

    return mvc.perform(
            post("/errands")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(createRequest))
                .accept(MediaType.APPLICATION_JSON));
  }

  /**
   * 테스트 준비용 의뢰 생성 요청을 보냅니다.
   *
   * @return 생성된 의뢰의 id
   * @throws Exception
   */
  private Long createErrandForGiven(String title, String content, PayDivision payDivision, Integer pay)
      throws Exception {

    MvcResult mvcResult = requestCreateErrand(title, content,payDivision,pay)
        .andReturn();

    String newErrandUri = mvcResult.getResponse().getHeader("Location");
    String newErrandId = newErrandUri.replace("/errands/", "");
    return Long.parseLong(newErrandId);
  }

  private ResultActions requestGetErrand(Long errandId) throws Exception {
    return mvc.perform(
            get("/errands/{id}", errandId)
                .contentType(MediaType.APPLICATION_JSON));
  }
}
