package com.dialog.server.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    UNEXPECTED_ERROR("1001", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST), // TODO: 예시이므로 ErrorCode 요소가 생기면 삭제


    ALREADY_SCRAPPED("5001", "이미 스크랩을 했습니다.", HttpStatus.BAD_REQUEST),
    NOT_SCRAPPED_YET("5002", "스크랩이 되어 있지 않습니다.", HttpStatus.BAD_REQUEST),

    ALREADY_LIKED("5010", "이미 좋아요를 눌렀습니다.", HttpStatus.BAD_REQUEST),
    NOT_LIKED_YET("5011", "좋아요를 누르지 않았습니다.", HttpStatus.BAD_REQUEST),
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
