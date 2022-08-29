package com.ahoo.issuetrackerserver.common.exception;

public class UnAuthorizedException extends ApplicationException {

    public UnAuthorizedException(ErrorType errorType) {
        super(errorType);
    }

    public UnAuthorizedException(ErrorType errorType, Throwable cause) {
        super(errorType, cause);
    }
}
