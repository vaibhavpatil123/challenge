package com.db.awmd.challenge.config;

public enum TransactionStatus {
    DEBIT_SUCCESS(1, "Successfully transfer amount"),
    DEBIT_FAIL(2, "Failed to transfer amount.Amount will deposit in 24 hrs."),
    DEPOSIT_FAIL(3, "Account deposited failed"), DEPOSIT_SUCCESS(4, "Account deposited successful");

    private final int    errorCode;
    private final String message;

    TransactionStatus(int code, String description) {
        this.errorCode = code;
        this.message   = description;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
