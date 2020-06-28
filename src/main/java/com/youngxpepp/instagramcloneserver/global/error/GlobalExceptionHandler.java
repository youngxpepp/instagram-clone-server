package com.youngxpepp.instagramcloneserver.global.error;

import com.youngxpepp.instagramcloneserver.global.error.exception.BusinessException;
import com.youngxpepp.instagramcloneserver.global.error.type.BindingErrorType;
import com.youngxpepp.instagramcloneserver.global.error.type.DefaultErrorType;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        DefaultErrorType errorType = new BindingErrorType(ErrorCode.INVALID_INPUT_VALUE, e.getBindingResult());

        return new ErrorResponse(errorType).responseEntity();
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwtException(ExpiredJwtException e) {
        DefaultErrorType errorType = new DefaultErrorType(ErrorCode.JWT_EXPIRED);
        return new ErrorResponse(errorType)
                .responseEntity();
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        DefaultErrorType errorType = new DefaultErrorType(e.getErrorCode());
        return new ErrorResponse(errorType)
                .responseEntity();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException e) {
        return new ErrorResponse(ErrorCode.AUTHENTICATION_FAILED)
                .responseEntity();
    }
}
