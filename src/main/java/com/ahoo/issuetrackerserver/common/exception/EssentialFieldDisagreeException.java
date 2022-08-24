package com.ahoo.issuetrackerserver.common.exception;

public class EssentialFieldDisagreeException extends ApplicationException {

    public EssentialFieldDisagreeException(ErrorType errorType) {
        super(errorType);
    }
}
