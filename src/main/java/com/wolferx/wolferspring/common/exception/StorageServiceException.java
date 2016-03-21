package com.wolferx.wolferspring.common.exception;

public class StorageServiceException extends BaseException {

    public StorageServiceException() {

        super();
    }

    public StorageServiceException(final String error) {

        super(error);
    }

    public StorageServiceException(final Throwable cause) {

        super(cause);
    }

    public StorageServiceException(final String error, final Throwable cause) {

        super(error, cause);
    }
}
