package com.ahoo.issuetrackerserver.common.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {

    private final ErrorType errorType;

    public ApplicationException(ErrorType errorType) {
        super(errorType.getErrorMessage());
        this.errorType = errorType;
    }

    public ApplicationException(ErrorType errorType, String errorMessage) {
        super(errorMessage + errorType.getErrorMessage());
        this.errorType = errorType;
    }

    public ApplicationException(ErrorType errorType, Throwable cause) {
        super(errorType.getErrorMessage(), cause);
        this.errorType = errorType;
    }
}
