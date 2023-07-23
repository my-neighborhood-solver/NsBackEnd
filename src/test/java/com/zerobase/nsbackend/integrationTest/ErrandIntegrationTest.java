package com.zerobase.nsbackend.integrationTest;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.zerobase.nsbackend.errand.domain.vo.PayDivision;
import com.zerobase.nsbackend.errand.dto.ErrandCreateRequest;
import com.zerobase.nsbackend.global.exceptionHandle.ErrorCode;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("의뢰 통합 테스트")
public class ErrandIntegrationTest extends IntegrationTest {

  @Test
  @DisplayName("의뢰 생성에 성공합니다.")
  void createErrand_success() throws Exception {
    // given
    ErrandCreateRequest createRequest = ErrandCreateRequest.builder()
        //.erranderId() //TODO: 회원 구현 되면 아이디 넣을 것
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
                .accept(MediaType.APPLICATION_JSON))
        .andDo(print());

    // then
    resultActions
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", CoreMatchers.startsWith("/errands/")));
  }

  @Test
  @DisplayName("의뢰 ID로 의뢰 조회에 성공합니다.")
  void readErrandById_success() throws Exception {
    // given
    ErrandCreateRequest createRequest = ErrandCreateRequest.builder()
        .title("제목")
        .content("내용")
        .payDivision(PayDivision.HOURLY)
        .pay(20000)
        .build();
    Long errandId = createErrandForTest(createRequest);

    // when
    ResultActions resultActions = mvc.perform(
            get("/errands/{id}", errandId)
                .contentType(MediaType.APPLICATION_JSON))
        .andDo(print());

    // then
    resultActions
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(errandId))
        .andExpect(jsonPath("$.title").value(is(createRequest.getTitle())))
        .andExpect(jsonPath("$.content").value(is(createRequest.getContent())))
        .andExpect(jsonPath("$.payDivision").value(is(createRequest.getPayDivision().name())))
        .andExpect(jsonPath("$.pay").value(is(createRequest.getPay())));
  }

  @Test
  @DisplayName("의뢰ID로 의뢰 조회 시, 의뢰 ID가 없어서 실패합니다.")
  void readErrandById_fail_because_errand_not_found() throws Exception {
    // given
    ErrandCreateRequest createRequest = ErrandCreateRequest.builder()
        .title("제목")
        .content("내용")
        .payDivision(PayDivision.HOURLY)
        .pay(20000)
        .build();

    Long notFoundId = 100L;

    // when
    ResultActions resultActions = mvc.perform(
            get("/errands/{id}", notFoundId)
                .contentType(MediaType.APPLICATION_JSON))
        .andDo(print());

    // then
    resultActions
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("errorCode").value(is(ErrorCode.ERRAND_NOT_FOUND.getCode())));
  }

  /**
   * 테스트 준비용 의뢰 생성 요청을 보냅니다.
   * @return 생성된 의뢰의 id
   * @throws Exception
   */
  private Long createErrandForTest(ErrandCreateRequest createRequest)
      throws Exception {

    MvcResult mvcResult = mvc.perform(
            post("/errands")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(createRequest))
                .accept(MediaType.APPLICATION_JSON))
        .andReturn();

    String newErrandUri = mvcResult.getResponse().getHeader("Location");
    String newErrandId = newErrandUri.replace("/errands/", "");
    return Long.parseLong(newErrandId);
  }
}
