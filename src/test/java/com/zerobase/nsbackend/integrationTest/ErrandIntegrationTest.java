package com.zerobase.nsbackend.integrationTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.zerobase.nsbackend.errand.domain.vo.PayDivision;
import com.zerobase.nsbackend.errand.dto.ErrandCreateRequest;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("의뢰 통합 테스트")
public class ErrandIntegrationTest extends IntegrationTest {

  @DisplayName("의뢰 생성에 성공합니다.")
  @Test
  public void createErrand_success() throws Exception {
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
}
