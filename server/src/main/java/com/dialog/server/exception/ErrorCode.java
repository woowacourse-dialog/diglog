package com.dialog.server.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    UNEXPECTED_ERROR("1001", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST), // TODO: 예시이므로 ErrorCode 요소가 생기면 삭제
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
