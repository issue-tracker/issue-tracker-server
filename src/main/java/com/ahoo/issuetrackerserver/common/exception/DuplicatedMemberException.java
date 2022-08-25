package com.ahoo.issuetrackerserver.common.exception;

public class DuplicatedMemberException extends ApplicationException {

    public DuplicatedMemberException(ErrorType errorType) {
        super(errorType);
    }

    public DuplicatedMemberException(ErrorType errorType, String errorMessage) {
        super(errorType, errorMessage);
    }
}
