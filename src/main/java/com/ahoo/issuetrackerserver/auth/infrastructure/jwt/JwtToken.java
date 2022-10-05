package com.ahoo.issuetrackerserver.auth.infrastructure.jwt;

import com.ahoo.issuetrackerserver.common.exception.ErrorType;
import javax.servlet.http.Cookie;

public interface JwtToken {

    String getToken();

    Cookie toCookie();

    ErrorType getErrorType();
}
