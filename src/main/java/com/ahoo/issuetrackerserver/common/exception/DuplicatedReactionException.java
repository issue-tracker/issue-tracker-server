package com.ahoo.issuetrackerserver.common.exception;

public class DuplicatedReactionException extends ApplicationException {

    public DuplicatedReactionException(ErrorType errorType) {
        super(errorType);
    }
}
