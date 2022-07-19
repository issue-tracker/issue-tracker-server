package com.ahoo.issuetrackerserver.member;

public enum AuthProvider {

    NONE("일반 회원가입"),
    GITHUB("깃허브"),
    NAVER("네이버"),
    KAKAO("카카오");

    private String providerName;

    AuthProvider(String providerName) {
        this.providerName = providerName;
    }

    public String getProviderName() {
        return providerName;
    }
}
