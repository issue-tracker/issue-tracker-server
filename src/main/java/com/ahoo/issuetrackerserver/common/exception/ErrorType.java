package com.ahoo.issuetrackerserver.common.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ErrorType {

    // 권한
    NO_AUTHORIZATION_HEADER(HttpStatus.UNAUTHORIZED, 1000, "요청에 Authorization 헤더가 존재하지 않습니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, 1001, "유효하지 않은 access_token입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, 1002, "유효하지 않은 refresh_token입니다."),
    INVALID_AUTHOR(HttpStatus.UNAUTHORIZED, 1003, "권한이 없는 사용자입니다."),
    NO_REFRESH_TOKEN_COOKIE(HttpStatus.BAD_REQUEST, 1004, "요청에 refresh_token 쿠키가 존재하지 않습니다."),

    // OAuth
    ESSENTIAL_FIELD_DISAGREE(HttpStatus.BAD_REQUEST, 2000, "필수 제공 동의 항목을 동의하지 않았습니다."),
    INVALID_CODE(HttpStatus.UNAUTHORIZED, 2001, "code가 유효하지 않습니다."),
    INVALID_AUTH_PROVIDER_TYPE(HttpStatus.UNAUTHORIZED, 2002, "유효하지 않은 AuthProviderType입니다."),

    // 회원가입/로그인
    DUPLICATED_ID(HttpStatus.BAD_REQUEST, 2100, "중복되는 아이디가 존재합니다."),
    DUPLICATED_NICKNAME(HttpStatus.BAD_REQUEST, 2101, "중복되는 닉네임이 존재합니다."),
    SIGN_IN_FAIL(HttpStatus.UNAUTHORIZED, 2102, "로그인에 실패했습니다. 아이디와 비밀번호를 다시 확인해주세요."),
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, 2103, "(으)로 이미 가입된 이메일입니다."),

    // DB에서 조회 불가
    NOT_EXISTS_ISSUE(HttpStatus.BAD_REQUEST, 3000, "존재하지 않는 이슈입니다."),
    NOT_EXISTS_MEMBER(HttpStatus.BAD_REQUEST, 3001, "존재하지 않는 회원입니다."),
    NOT_EXISTS_LABEL(HttpStatus.BAD_REQUEST, 3002, "존재하지 않는 라벨입니다."),
    NOT_EXISTS_COMMENT(HttpStatus.BAD_REQUEST, 3003, "존재하지 않는 코멘트입니다."),
    NOT_EXISTS_MILESTONE(HttpStatus.BAD_REQUEST, 3004, "존재하지 않는 마일스톤입니다."),
    NOT_EXISTS_REACTION(HttpStatus.BAD_REQUEST, 3005, "존재하지 않는 리액션입니다."),

    // 파일 업로드
    INVALID_CONTENT_TYPE(HttpStatus.BAD_REQUEST, 4001, "이미지 파일만 업로드 가능합니다."),
    FILE_CONVERT_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, 4002, "파일 변환 중 에러가 발생하였습니다."),
    INVALID_FILE_NAME(HttpStatus.BAD_REQUEST, 4003, "잘못된 형식의 파일 이름입니다."),

    // 기타 유효성 검증
    INVALID_HEX_COLOR_CODE(HttpStatus.BAD_REQUEST, 5001, "유효하지 않은 색상 코드입니다."),
    DUPLICATED_LABEL_TITLE(HttpStatus.BAD_REQUEST, 5002, "중복되는 라벨 이름이 존재합니다."),
    DUPLICATED_REACTION(HttpStatus.BAD_REQUEST, 5003, "중복되는 리액션이 존재합니다."),
    DUPLICATED_MILESTONE_TITLE(HttpStatus.BAD_REQUEST, 5004, "중복되는 마일스톤 이름이 존재합니다."),
    NOT_DELETABLE_COMMENT(HttpStatus.BAD_REQUEST, 6001, "삭제할 수 없는 코멘트입니다."),
    NOT_MATCHED_MILESTONE(HttpStatus.BAD_REQUEST, 6002, "삭제하려는 마일스톤이 해당 이슈에 존재하지 않습니다.");

    private final HttpStatus status;
    private final int errorCode;
    private final String errorMessage;
}
