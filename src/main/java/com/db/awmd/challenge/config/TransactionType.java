package com.db.awmd.challenge.config;

public enum TransactionType {
    DEPOSIT(1, "Deposit"), DEBIT(2, "Debit");

    private final int    statusCode;
    private final String message;

    TransactionType(int code, String description) {
        this.statusCode = code;
        this.message    = description;
    }

    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }
}

// ~ Formatted by Jindent --- http://www.jindent.com


//~ Formatted by Jindent --- http://www.jindent.com
