package com.dialog.server.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    GITHUB_USER_ID_MISSING("1001", "GitHub에서 사용자 ID를 가져올 수 없습니다.", HttpStatus.BAD_GATEWAY),

    USER_NOT_FOUND("5031", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    EXIST_USER_EMAIL("5032", "이미 존재하는 이메일입니다.", HttpStatus.BAD_REQUEST),
    REGISTERED_USER("5033", "이미 회원가입한 회원입니다.", HttpStatus.BAD_REQUEST),
    NOT_REGISTERED_USER("5034", "회원가입하지 않은 회원입니다.", HttpStatus.BAD_REQUEST),
    ;

    public final String code;
    public final String message;
    public final HttpStatus status;

    ErrorCode(String code,
              String message,
              HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
