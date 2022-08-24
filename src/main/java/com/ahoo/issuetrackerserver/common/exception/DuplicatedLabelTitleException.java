package com.ahoo.issuetrackerserver.common.exception;

public class DuplicatedLabelTitleException extends ApplicationException {

    public DuplicatedLabelTitleException(ErrorType errorType) {
        super(errorType);
    }
}
