package com.db.awmd.challenge.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseAppException extends RuntimeException {
    private final int errorCode;

    public BaseAppException(String message, int errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
