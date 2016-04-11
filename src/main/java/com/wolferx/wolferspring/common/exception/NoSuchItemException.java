package com.wolferx.wolferspring.common.exception;

import com.wolferx.wolferspring.common.constant.ErrorCode;

public class NoSuchItemException extends BaseServiceException {

    public NoSuchItemException() {

        super();
    }

    public NoSuchItemException(final String error) {

        super(error);
    }

    public NoSuchItemException(final Throwable cause) {

        super(cause);
    }

    public NoSuchItemException(final String error, final Throwable cause) {

        super(error, cause);
    }

    public NoSuchItemException(final ErrorCode errorCode) {

        super(errorCode);
    }

    public NoSuchItemException(final String error, final ErrorCode errorCode) {

        super(error, errorCode);
    }
}
