package com.ahoo.issuetrackerserver.auth;

import javax.servlet.http.Cookie;

public interface JwtToken {

    String getToken();

    Cookie toCookie();
}
