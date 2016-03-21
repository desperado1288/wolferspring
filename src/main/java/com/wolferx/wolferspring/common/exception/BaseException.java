package com.wolferx.wolferspring.common.exception;

import com.wolferx.wolferspring.common.ErrorCode;

public class BaseException extends Exception {

    private ErrorCode errorCode;

    BaseException() {
        super();
    }

    BaseException(final String error) {
        super(error);
    }

    BaseException(final Throwable cause) {
        super(cause);
    }

    BaseException(final String error, final Throwable cause) {
        super(error, cause);
    }

    public BaseException(final ErrorCode errorCode) {
        super(errorCode.toString());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
