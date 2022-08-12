package com.ahoo.issuetrackerserver.auth.infrastructure.jwt;

import javax.servlet.http.Cookie;

public interface JwtToken {

    String getToken();

    Cookie toCookie();
}
