package com.ahoo.issuetrackerserver.exception;

public class IllegalAuthProviderTypeException extends RuntimeException {

    public IllegalAuthProviderTypeException(Throwable cause) {
        super("유효하지 않은 AuthProviderType입니다.", cause);
    }
}
