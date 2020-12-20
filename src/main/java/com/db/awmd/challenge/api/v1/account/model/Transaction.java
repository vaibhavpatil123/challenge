package com.db.awmd.challenge.api.v1.account.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class Transaction {
  @NotNull
  @Min(value = 0, message = "Transaction amount must be positive.")
  private BigDecimal amount;

  @NotBlank(message = "From account value is mandatory")
  @NotNull
  @NotEmpty
  private String fromAccountId;

  @NotBlank(message = "To Account value is mandatory")
  @NotNull
  @NotEmpty
  private String toAccountId;
}

// ~ Formatted by Jindent --- http://www.jindent.com
