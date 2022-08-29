package com.ahoo.issuetrackerserver.auth.domain;

import com.ahoo.issuetrackerserver.auth.presentation.dto.AuthUserResponse;
import java.util.function.Function;
import org.json.JSONObject;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public enum AuthProvider {

    NONE("일반 회원가입", null, null, null, null),
    GITHUB("깃허브",
        (code) -> {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add(AuthProvider.CLIENT_ID, System.getenv("GITHUB_CLIENT_ID"));
            formData.add(AuthProvider.CLIENT_SECRET, System.getenv("GITHUB_CLIENT_SECRET"));
            formData.add(AuthProvider.CODE, code);
            return formData;
        },
        "https://github.com/login/oauth/access_token",
        "https://api.github.com/user",
        (json) -> new AuthUserResponse(
            String.valueOf(json.getBigInteger(AuthProvider.ID)),
            json.getString(AuthProvider.EMAIL),
            json.getString("avatar_url")
        )
    ),
    NAVER("네이버",
        (code) -> {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add(AuthProvider.CLIENT_ID, System.getenv("NAVER_CLIENT_ID"));
            formData.add(AuthProvider.CLIENT_SECRET, System.getenv("NAVER_CLIENT_SECRET"));
            formData.add(AuthProvider.CODE, code);
            formData.add(AuthProvider.GRANT_TYPE, AuthProvider.AUTHORIZATION_CODE);
            formData.add("state", System.getenv("NAVER_STATE"));
            return formData;
        },
        "https://nid.naver.com/oauth2.0/token",
        "https://openapi.naver.com/v1/nid/me",
        (json) -> {
            JSONObject response = json.getJSONObject("response");
            return new AuthUserResponse(
                response.getString(AuthProvider.ID),
                response.getString(AuthProvider.EMAIL),
                response.getString("profile_image")
            );
        }
    ),
    KAKAO("카카오",
        (code) -> {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add(AuthProvider.CLIENT_ID, System.getenv("KAKAO_CLIENT_ID"));
            formData.add(AuthProvider.CLIENT_SECRET, System.getenv("KAKAO_CLIENT_SECRET"));
            formData.add(AuthProvider.CODE, code);
            formData.add(AuthProvider.GRANT_TYPE, AuthProvider.AUTHORIZATION_CODE);
            return formData;
        },
        "https://kauth.kakao.com/oauth/token",
        "https://kapi.kakao.com/v2/user/me",
        (json) -> {
            JSONObject kakaoAccount = json.getJSONObject("kakao_account");
            JSONObject profile = kakaoAccount.getJSONObject("profile");
            return new AuthUserResponse(
                String.valueOf(json.getBigInteger(AuthProvider.ID)),
                kakaoAccount.getString(AuthProvider.EMAIL),
                profile.getString("profile_image_url")
            );
        }
    );

    private static final String CLIENT_ID = "client_id";
    private static final String CLIENT_SECRET = "client_secret";
    private static final String CODE = "code";
    private static final String AUTHORIZATION_CODE = "authorization_code";
    private static final String GRANT_TYPE = "grant_type";
    private static final String ID = "id";
    private static final String EMAIL = "email";

    private String providerName;
    private Function<String, MultiValueMap<String, String>> createAccessTokenRequest;
    private String requestAccessTokenUrl;
    private String requestAuthUserUrl;
    private Function<JSONObject, AuthUserResponse> parseAuthUserResponse;

    AuthProvider(String providerName, Function<String, MultiValueMap<String, String>> createAccessTokenRequest,
        String requestAccessTokenUrl, String requestAuthUserUrl,
        Function<JSONObject, AuthUserResponse> parseAuthUserResponse) {
        this.providerName = providerName;
        this.createAccessTokenRequest = createAccessTokenRequest;
        this.requestAccessTokenUrl = requestAccessTokenUrl;
        this.requestAuthUserUrl = requestAuthUserUrl;
        this.parseAuthUserResponse = parseAuthUserResponse;
    }

    public String getProviderName() {
        return providerName;
    }

    public String getRequestAccessTokenUrl() {
        return requestAccessTokenUrl;
    }

    public String getRequestAuthUserUrl() {
        return requestAuthUserUrl;
    }

    public MultiValueMap<String, String> createAccessTokenRequest(String code) {
        return createAccessTokenRequest.apply(code);
    }

    public AuthUserResponse parseAuthUserResponse(JSONObject response) {
        return parseAuthUserResponse.apply(response);
    }
}
