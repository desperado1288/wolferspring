package com.wolferx.wolferspring.common.exception;

import com.wolferx.wolferspring.common.constant.ErrorCode;

public class InsertItemAlreadyExistException extends BaseServiceException {

    public InsertItemAlreadyExistException() {

        super();
    }

    public InsertItemAlreadyExistException(final String error) {

        super(error);
    }

    public InsertItemAlreadyExistException(final Throwable cause) {

        super(cause);
    }

    public InsertItemAlreadyExistException(final String error, final Throwable cause) {

        super(error, cause);
    }

    public InsertItemAlreadyExistException(final ErrorCode errorCode) {

        super(errorCode);
    }
}
