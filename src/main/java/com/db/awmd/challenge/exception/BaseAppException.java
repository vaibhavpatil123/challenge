package com.db.awmd.challenge.exception;

import lombok.Data;

@Data
public class BaseAppException extends RuntimeException {
  private final int errorCode;
  public BaseAppException(String message, int errorCode, Throwable cause) {
    super(message, cause);
    this.errorCode = errorCode;
  }
}
