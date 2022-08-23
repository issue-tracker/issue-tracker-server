package com.ahoo.issuetrackerserver.common.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorMessage {

    // auth
    public static final String ESSENTIAL_FIELD_DISAGREE = "필수 제공 동의 항목을 동의하지 않았습니다.";
    public static final String INVALID_TOKEN = "유효하지 않은 토큰입니다.";
    public static final String INVALID_REFRESH_TOKEN = "유효하지 않은 refresh_token입니다.";
    public static final String INVALID_CODE = "code가 유효하지 않습니다.";
    public static final String INVALID_AUTHOR = "권한이 없는 사용자입니다.";

    // member
    public static final String SIGN_IN_FAIL = "로그인에 실패했습니다. 아이디와 비밀번호를 다시 확인해주세요.";
    public static final String DUPLICATED_ID = "중복되는 아이디가 존재합니다.";
    public static final String DUPLICATED_NICKNAME = "중복되는 닉네임이 존재합니다.";
    public static final String NOT_EXISTS_MEMBER = "존재하지 않는 회원입니다.";
    public static final String DUPLICATED_EMAIL = "(으)로 이미 가입된 이메일입니다.";

    // milestone
    public static final String NOT_EXISTS_MILESTONE = "존재하지 않는 마일스톤입니다.";

    // argument resolver
    public static final String NO_AUTHORIZATION_HEADER = "요청에 Authorization 헤더가 존재하지 않습니다.";

    // label
    public static final String DUPLICATED_LABEL_TITLE = "중복되는 라벨 이름이 존재합니다.";
    public static final String NOT_EXISTS_LABEL = "존재하지 않는 라벨입니다.";
    public static final String INVALID_HEX_COLOR_CODE = "유효하지않은 색상 코드입니다.";

    // issue
    public static final String NOT_EXISTS_ISSUE = "존재하지 않는 이슈입니다.";

    // comment
    public static final String NOT_EXISTS_COMMENT = "존재하지 않는 코멘트입니다.";

    // reaction
    public static final String DUPLICATED_REACTION = "중복되는 리액션이 존재합니다.";
}
