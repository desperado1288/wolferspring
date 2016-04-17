package com.wolferx.wolferspring.common.exception;

import com.wolferx.wolferspring.common.constant.ErrorCode;

public class ItemNotFoundException extends BaseServiceException {

    public ItemNotFoundException() {
        super("Item not found");
    }

    public ItemNotFoundException(final String error) {
        super(error);
    }

    public ItemNotFoundException(final Throwable cause) {
        super(cause);
    }

    public ItemNotFoundException(final String error, final Throwable cause) {
        super(error, cause);
    }

    public ItemNotFoundException(final ErrorCode errorCode) {
        super(errorCode);
    }

    public ItemNotFoundException(final String error, final ErrorCode errorCode) {
        super(error, errorCode);
    }
}
