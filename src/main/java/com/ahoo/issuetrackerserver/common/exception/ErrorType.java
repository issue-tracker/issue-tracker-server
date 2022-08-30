package com.ahoo.issuetrackerserver.common.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ErrorType {

    ESSENTIAL_FIELD_DISAGREE(HttpStatus.BAD_REQUEST, "필수 제공 동의 항목을 동의하지 않았습니다."),
    NO_AUTHORIZATION_HEADER(HttpStatus.UNAUTHORIZED, "요청에 Authorization 헤더가 존재하지 않습니다."),
    SIGN_IN_FAIL(HttpStatus.UNAUTHORIZED, "로그인에 실패했습니다. 아이디와 비밀번호를 다시 확인해주세요."),
    FILE_CONVERT_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "파일 변환 중 에러가 발생하였습니다."),

    INVALID_CODE(HttpStatus.UNAUTHORIZED, "code가 유효하지 않습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 refresh_token입니다."),
    INVALID_AUTHOR(HttpStatus.UNAUTHORIZED, "권한이 없는 사용자입니다."),
    INVALID_AUTH_PROVIDER_TYPE(HttpStatus.UNAUTHORIZED, "유효하지 않은 AuthProviderType입니다."),
    INVALID_HEX_COLOR_CODE(HttpStatus.BAD_REQUEST, "유효하지 않은 색상 코드입니다."),
    INVALID_FILE_NAME(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일 이름입니다."),
    INVALID_CONTENT_TYPE(HttpStatus.BAD_REQUEST, "이미지 파일만 업로드 가능합니다."),

    DUPLICATED_ID(HttpStatus.BAD_REQUEST, "중복되는 아이디가 존재합니다."),
    DUPLICATED_NICKNAME(HttpStatus.BAD_REQUEST, "중복되는 닉네임이 존재합니다."),
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "(으)로 이미 가입된 이메일입니다."),
    DUPLICATED_REACTION(HttpStatus.BAD_REQUEST, "중복되는 리액션이 존재합니다."),
    DUPLICATED_LABEL_TITLE(HttpStatus.BAD_REQUEST, "중복되는 라벨 이름이 존재합니다."),

    NOT_EXISTS_MEMBER(HttpStatus.BAD_REQUEST, "존재하지 않는 회원입니다."),
    NOT_EXISTS_LABEL(HttpStatus.BAD_REQUEST, "존재하지 않는 라벨입니다."),
    NOT_EXISTS_ISSUE(HttpStatus.BAD_REQUEST, "존재하지 않는 이슈입니다."),
    NOT_EXISTS_COMMENT(HttpStatus.BAD_REQUEST, "존재하지 않는 코멘트입니다."),
    NOT_EXISTS_MILESTONE(HttpStatus.BAD_REQUEST, "존재하지 않는 마일스톤입니다."),
    NOT_EXISTS_REACTION(HttpStatus.BAD_REQUEST, "존재하지 않는 리액션입니다.");

    private final HttpStatus status;
    private final String errorMessage;
}
