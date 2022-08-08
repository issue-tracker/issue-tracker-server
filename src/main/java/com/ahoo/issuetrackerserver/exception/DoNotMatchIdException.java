package com.ahoo.issuetrackerserver.exception;

public class DoNotMatchIdException extends RuntimeException {

    public DoNotMatchIdException(String message) {
        super(message);
    }
}
