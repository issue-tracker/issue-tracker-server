package com.ahoo.issuetrackerserver.auth.infrastructure.jwt;

import com.ahoo.issuetrackerserver.common.exception.ErrorType;
import org.springframework.http.ResponseCookie;

public interface JwtToken {

    String getToken();

    ResponseCookie toCookie();

    ErrorType getErrorType();
}
