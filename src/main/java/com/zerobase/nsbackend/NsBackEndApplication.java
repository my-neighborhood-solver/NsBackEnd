package com.zerobase.nsbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class NsBackEndApplication {

  public static void main(String[] args) {
    SpringApplication.run(NsBackEndApplication.class, args);
  }

}
