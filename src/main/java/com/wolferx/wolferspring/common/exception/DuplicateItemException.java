package com.wolferx.wolferspring.common.exception;

import com.wolferx.wolferspring.common.constant.ErrorCode;

public class DuplicateItemException extends BaseServiceException {

    public DuplicateItemException() {

        super("Duplicate item already exist");
    }

    public DuplicateItemException(final String error) {

        super(error);
    }

    public DuplicateItemException(final Throwable cause) {

        super("Duplicate item already exist", cause);
    }

    public DuplicateItemException(final String error, final Throwable cause) {

        super(error, cause);
    }

    public DuplicateItemException(final ErrorCode errorCode) {

        super(errorCode);
    }

    public DuplicateItemException(final String error, final ErrorCode errorCode) {

        super(error, errorCode);
    }
}
