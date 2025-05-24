package com.dialog.server.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    UNEXPECTED_ERROR("1001", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST), // TODO: 예시이므로 ErrorCode 요소가 생기면 삭제

    NOT_FOUND_USER("5021", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_DISCUSSION("5022", "토론을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ALREADY_PARTICIPATION_DISCUSSION("5023", "이미 참여 중인 토론입니다.", HttpStatus.BAD_REQUEST),
    PARTICIPATION_LIMIT_EXCEEDED("5024", "최대 참여자 수를 초과했습니다.", HttpStatus.BAD_REQUEST),

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
