package com.db.awmd.challenge.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class AppEnvParameters {
    @Value("${account.min_bal_limit}")
    private String accountMinBalanceLimit;
}

// ~ Formatted by Jindent --- http://www.jindent.com


//~ Formatted by Jindent --- http://www.jindent.com
