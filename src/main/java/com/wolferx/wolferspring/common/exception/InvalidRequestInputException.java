package com.wolferx.wolferspring.common.exception;

import com.wolferx.wolferspring.common.constant.ErrorCode;

public class InvalidRequestInputException extends BaseServiceException {

    public InvalidRequestInputException() {

        super();
    }

    public InvalidRequestInputException(final String error) {

        super(error);
    }

    public InvalidRequestInputException(final Throwable cause) {

        super(cause);
    }

    public InvalidRequestInputException(final String error, final Throwable cause) {

        super(error, cause);
    }

    public InvalidRequestInputException(final ErrorCode errorCode) {

        super(errorCode);
    }

    public InvalidRequestInputException(final String error, final ErrorCode errorCode) {

        super(error, errorCode);
    }
}
