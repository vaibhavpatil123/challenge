package com.db.awmd.challenge.config;

public enum APPTransactionMessages {
    ACCOUNT_DUPLICATE(4000, "This account already exists in system."),
    TRANSACTION_FAILED_NOT_ALLOW_SAME_ACCOUNT(4001, "You can transfer money only to other account"),
    TRANSACTION_FAILED_TO_ROLLBACK(4002, "Failed to rollback transaction please contact bank."),
    ACCOUNT_TRANSACTION_LOW_BALANCE(4003, "This account insufficient balance."),
    SYSTEM_ERROR(4004, "Transaction failed contact admin."),
    ACCOUNT_TRANSACTION_MINIMUM_AMOUNT_LIMIT(4005, "Minimum amount validation check failed"),
    ACCOUNT_NOT_AVAILABLE(4006, "This account not exists in system.");

    private final int    errorCode;
    private final String message;

    APPTransactionMessages(int code, String description) {
        this.errorCode = code;
        this.message   = description;
    }

    @Override
    public String toString() {
        return errorCode + ": " + message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }
}

// ~ Formatted by Jindent --- http://www.jindent.com


//~ Formatted by Jindent --- http://www.jindent.com
