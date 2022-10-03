package com.ahoo.issuetrackerserver.common.exception;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException e) {
        return ResponseEntity.status(e.getErrorType().getStatus())
            .body(new ErrorResponse(e.getErrorType().getErrorCode(), e.getErrorType().getErrorMessage()));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();

        StringBuilder message = new StringBuilder();
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                message.append(fieldError.getField());
                message.append(": ");
                message.append(fieldError.getDefaultMessage());
                message.append(" ");
            }
        }

        return ResponseEntity.badRequest().body(new ErrorResponse(0, message.toString()));
    }

    @ExceptionHandler(value = MissingRequestCookieException.class)
    public ResponseEntity<ErrorResponse> handleMissingRequestCookieException(MissingRequestCookieException e) {
        //TODO: 현재는 쿠키를 리프레쉬 토큰만 사용하고 있어 문제가 되지 않지만, 다른 쿠키를 사용하게 되면 현재의 방법은 문제가 될 것
        // 다른 방법도 생각하기!
        return ResponseEntity.badRequest()
            .body(new ErrorResponse(
                ErrorType.NO_REFRESH_TOKEN_COOKIE.getErrorCode(),
                ErrorType.NO_REFRESH_TOKEN_COOKIE.getErrorMessage()));
    }
}
