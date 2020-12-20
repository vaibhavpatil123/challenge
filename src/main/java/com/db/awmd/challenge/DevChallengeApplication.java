package com.db.awmd.challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@SpringBootApplication
@EnableSwagger2
public class DevChallengeApplication {
  private ApiInfo apiInfo() {
    return new ApiInfo(
        "Account Manager API",
        "For more info visit https://git/",
        "1.0",
        "",
        new Contact("Vaibhav Patil", "", "vaibhav.h.patil@gmail.com"),
        "apache 2",
        "",
        Collections.emptyList());
  }

  public static void main(String[] args) {
    SpringApplication.run(DevChallengeApplication.class, args);
  }

  @Bean
  public Docket productApi() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.db.awmd.challenge.api.v1.account"))
        .build()
        .apiInfo(apiInfo());
  }
}

// ~ Formatted by Jindent --- http://www.jindent.com
