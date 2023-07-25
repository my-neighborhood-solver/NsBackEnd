package com.zerobase.nsbackend.integrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * 통합테스트 베이스 클래스입니다.
 * 통합테스트 시 상속 받아 사용합니다.
 */
@Disabled
@Transactional
@AutoConfigureMockMvc
@SpringBootTest
public class IntegrationTest {
  @Autowired
  protected MockMvc mvc;
  @Autowired
  protected ObjectMapper objectMapper;

  protected String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
