package com.dialog.server.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnExpectedException(Exception ex) {
        return ResponseEntity.internalServerError()
                .body(new ApiErrorResponse("0000", "예기치 못한 에러가 발생하였습니다."));
    }

    @ExceptionHandler(DialogException.class)
    public ResponseEntity<ApiErrorResponse> handleUnExpectedException(DialogException ex) {
        return ResponseEntity.status(ex.getStatus()).body(ApiErrorResponse.from(ex));
    }
}
