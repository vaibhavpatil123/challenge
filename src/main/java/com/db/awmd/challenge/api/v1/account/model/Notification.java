package com.db.awmd.challenge.api.v1.account.model;

import com.db.awmd.challenge.config.TransactionStatus;
import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.service.impl.utility.AppUtility;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Notification {
  private String transactionDate = AppUtility.getCurrentDate();
  private Account account;
  private BigDecimal amount;
  private String transactionId;
  private String transactionDetails;
  private String transactionStatus;

  public Notification(
      Account account,
      BigDecimal amount,
      String transactionId,
      TransactionStatus transactionStatus) {
    this.account = account;
    this.amount = amount;
    this.transactionId = transactionId;
    this.transactionStatus = "" + transactionStatus.getErrorCode();
    this.transactionDetails = transactionStatus.getMessage();
  }

  @Override
  public String toString() {
    return "Notification{"
        + "account="
        + account
        + ", amount="
        + amount
        + ", transactionId='"
        + transactionId
        + '\''
        + ", transactionDetails='"
        + transactionDetails
        + '\''
        + ", transactionDate='"
        + transactionDate
        + '\''
        + ", transactionStatus='"
        + transactionStatus
        + '\''
        + '}';
  }
}

// ~ Formatted by Jindent --- http://www.jindent.com
