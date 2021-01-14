package com.db.awmd.challenge.api.v1.account.model;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;

@Data
public class Transaction {
    @NotNull
    @Min(
        value   = 0,
        message = "Transaction amount must be positive."
    )
    private BigDecimal amount;
    @NotBlank(message = "From account value is mandatory")
    @NotNull
    @NotEmpty
    private String     fromAccountId;
    @NotBlank(message = "To Account value is mandatory")
    @NotNull
    @NotEmpty
    private String     toAccountId;
}

// ~ Formatted by Jindent --- http://www.jindent.com


//~ Formatted by Jindent --- http://www.jindent.com
