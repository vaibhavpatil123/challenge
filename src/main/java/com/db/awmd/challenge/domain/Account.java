package com.db.awmd.challenge.domain;

import java.math.BigDecimal;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Account {
    @JsonIgnore
    private final Lock   lock             = new ReentrantLock();
    @JsonIgnore
    private BigDecimal   lastCloseBalance = new BigDecimal(0);
    @NotBlank(message = "Account value is mandatory")
    @NotNull
    @NotEmpty
    private final String accountId;
    @NotNull
    @Min(
        value   = 0,
        message = "Initial balance must be positive."
    )
    private BigDecimal   balance;
    @JsonIgnore
    private BigDecimal   lastBalance;

    @JsonCreator
    public Account(@JsonProperty("accountId") String accountId, @JsonProperty("balance") BigDecimal balance) {
        this.accountId = accountId;
        this.balance   = balance;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
