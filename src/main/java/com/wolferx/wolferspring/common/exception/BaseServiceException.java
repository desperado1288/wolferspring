package com.wolferx.wolferspring.common.exception;

import com.wolferx.wolferspring.common.constant.ErrorCode;

public class BaseServiceException extends Exception {

    private ErrorCode errorCode;

    BaseServiceException() {
        super();
    }

    public BaseServiceException(final String error) {
        super(error);
    }

    public BaseServiceException(final Throwable cause) {
        super(cause);
    }

    public BaseServiceException(final String error, final Throwable cause) {
        super(error, cause);
    }

    public BaseServiceException(final ErrorCode errorCode) {
        super(errorCode.toString());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
