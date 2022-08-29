package com.ahoo.issuetrackerserver.common.exception;

public class IllegalAuthProviderTypeException extends ApplicationException {

    public IllegalAuthProviderTypeException(ErrorType errorType, Throwable cause) {
        super(errorType, cause);
    }
}
