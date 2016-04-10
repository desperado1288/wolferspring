package com.wolferx.wolferspring.common.exception;

import com.wolferx.wolferspring.common.constant.ErrorCode;

public class InvalidInputException extends BaseServiceException {

    public InvalidInputException() {

        super();
    }

    public InvalidInputException(final String error) {

        super(error);
    }

    public InvalidInputException(final Throwable cause) {

        super(cause);
    }

    public InvalidInputException(final String error, final Throwable cause) {

        super(error, cause);
    }

    public InvalidInputException(final ErrorCode errorCode) {

        super(errorCode);
    }
}
