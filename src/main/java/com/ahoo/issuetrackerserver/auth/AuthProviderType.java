package com.ahoo.issuetrackerserver.auth;

public enum AuthProviderType {

    NONE("일반 회원가입"),
    GITHUB("깃허브"),
    NAVER("네이버"),
    KAKAO("카카오");

    private String providerName;

    AuthProviderType(String providerName) {
        this.providerName = providerName;
    }

    public String getProviderName() {
        return providerName;
    }
}
