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
            formData.add("client_id", System.getenv("GITHUB_CLIENT_ID"));
            formData.add("client_secret", System.getenv("GITHUB_CLIENT_SECRET"));
            formData.add("code", code);
            return formData;
        },
        "https://github.com/login/oauth/access_token",
        "https://api.github.com/user",
        (json) -> new AuthUserResponse(
            String.valueOf(json.getBigInteger("id")),
            json.getString("email"),
            json.getString("avatar_url")
        )
    ),
    NAVER("네이버",
        (code) -> {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("client_id", System.getenv("NAVER_CLIENT_ID"));
            formData.add("client_secret", System.getenv("NAVER_CLIENT_SECRET"));
            formData.add("code", code);
            formData.add("grant_type", "authorization_code");
            formData.add("state", System.getenv("NAVER_STATE"));
            return formData;
        },
        "https://nid.naver.com/oauth2.0/token",
        "https://openapi.naver.com/v1/nid/me",
        (json) -> {
            JSONObject response = json.getJSONObject("response");
            return new AuthUserResponse(
                response.getString("id"),
                response.getString("email"),
                response.getString("profile_image")
            );
        }
    ),
    KAKAO("카카오",
        (code) -> {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("client_id", System.getenv("KAKAO_CLIENT_ID"));
            formData.add("client_secret", System.getenv("KAKAO_CLIENT_SECRET"));
            formData.add("code", code);
            formData.add("grant_type", "authorization_code");
            return formData;
        },
        "https://kauth.kakao.com/oauth/token",
        "https://kapi.kakao.com/v2/user/me",
        (json) -> {
            JSONObject kakaoAccount = json.getJSONObject("kakao_account");
            JSONObject profile = kakaoAccount.getJSONObject("profile");
            return new AuthUserResponse(
                String.valueOf(json.getBigInteger("id")),
                kakaoAccount.getString("email"),
                profile.getString("profile_image_url")
            );
        }
    );

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
