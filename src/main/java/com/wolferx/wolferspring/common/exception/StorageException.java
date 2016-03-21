package com.wolferx.wolferspring.common.exception;

import com.wolferx.wolferspring.common.ErrorCode;

public class StorageException extends BaseException {

    public StorageException() {

        super();
    }

    public StorageException(final String error) {

        super(error);
    }

    public StorageException(final Throwable cause) {

        super(cause);
    }

    public StorageException(final String error, final Throwable cause) {

        super(error, cause);
    }

    public StorageException(final ErrorCode errorCode) {

        super(errorCode);
    }
}
