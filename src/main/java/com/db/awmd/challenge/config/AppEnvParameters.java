package com.db.awmd.challenge.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class AppEnvParameters {
  @Value("${account.min_bal_limit}")
  private String accountMinBalanceLimit;
}

// ~ Formatted by Jindent --- http://www.jindent.com
