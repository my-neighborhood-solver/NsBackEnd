package com.zerobase.nsbackend.integrationTest;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import com.zerobase.nsbackend.errand.domain.vo.PayDivision;
import com.zerobase.nsbackend.errand.dto.ErrandCreateRequest;
import com.zerobase.nsbackend.errand.dto.ErrandUpdateRequest;
import com.zerobase.nsbackend.global.exceptionHandle.ErrorCode;
import com.zerobase.nsbackend.member.domain.Member;
import com.zerobase.nsbackend.member.repository.MemberRepository;
import com.zerobase.nsbackend.member.type.Authority;
import com.zerobase.nsbackend.member.type.Role;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

@DisplayName("의뢰 통합 테스트")
class ErrandIntegrationTest extends IntegrationTest {

  @Autowired
  MemberRepository memberRepository;

  Member member01;
  Member member02;
  @BeforeEach
  void init() {
    member01 = Member.builder()
        .email("member01")
        .password("password")
        .authority(Authority.ROLE_USER)
        .build();
    member02 = Member.builder()
        .email("member02")
        .password("password")
        .authority(Authority.ROLE_USER)
        .build();
    memberRepository.save(member01);
    memberRepository.save(member02);
  }

  @Test
  @DisplayName("의뢰 생성에 성공합니다.")
  void createErrand_success() throws Exception {
    // given
    ErrandCreateRequest request = ErrandCreateRequest.builder()
        .title("testTitle")
        .content("testContent")
        .payDivision(PayDivision.HOURLY)
        .pay(10000)
        .build();

    // when
    ResultActions resultActions = requestCreateErrand(request.getTitle(), request.getContent(),
        request.getPayDivision(), request.getPay());

    // then
    resultActions
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", CoreMatchers.startsWith("/errands/")));
  }

  @WithMockUser(roles = "USER")
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

  @WithMockUser(roles = "USER")
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
            .with(user(member01.getEmail()))
            .content(asJsonString(updateRequest))
            .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    resultActions
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("의뢰 성공적으로 수정 시, 의뢰자가 아니라서 실패합니다.")
  void updateErrand_fail_because_not_errander() throws Exception {
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
            .with(user(member02.getEmail()))      // member02
            .content(asJsonString(updateRequest))
            .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    resultActions
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("의뢰 취소에 성공합니다.")
  void cancelErrand_success() throws Exception {
    Long errandId = createErrandForGiven("testTitle", "testContent", PayDivision.UNIT, 1000);

    ResultActions perform = mvc.perform(
        post("/errands/{id}/cancel", errandId)
        .with(user(member01.getEmail())));

    perform.andExpect(status().isOk());
  }

  @Test
  @DisplayName("의뢰자가 아니라서 의뢰 취소에 실패합니다.")
  void cancelErrand_fail_because_not_errander() throws Exception {
    Long errandId = createErrandForGiven("testTitle", "testContent", PayDivision.UNIT, 1000);

    ResultActions perform = mvc.perform(
        post("/errands/{id}/cancel", errandId)
            .with(user(member02.getEmail())));    // member02

    perform.andExpect(status().isBadRequest());
  }

  private ResultActions requestCreateErrand(String title, String content, PayDivision payDivision, Integer pay)
      throws Exception {
    ErrandCreateRequest createRequest = ErrandCreateRequest.builder()
        .title(title)
        .content(content)
        .payDivision(payDivision)
        .pay(pay)
        .build();

    MockMultipartFile jsonRequest = new MockMultipartFile("errand", "",
        "application/json", asJsonString(createRequest).getBytes());

    return mvc.perform(
        multipart("/errands")
            .file(jsonRequest)
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .with(user(member01.getEmail()))
    );
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
