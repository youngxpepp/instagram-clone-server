package com.youngxpepp.instagramcloneserver.global.error;

import com.youngxpepp.instagramcloneserver.global.error.exception.BusinessException;
import com.youngxpepp.instagramcloneserver.global.error.exception.NoAuthorizationException;
import com.youngxpepp.instagramcloneserver.global.error.exception.NoPrefixJwtException;
import com.youngxpepp.instagramcloneserver.global.error.type.BindingErrorType;
import com.youngxpepp.instagramcloneserver.global.error.type.DefaultErrorType;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
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

    @ExceptionHandler(NoAuthorizationException.class)
    public ResponseEntity<ErrorResponse> handleNoAuthorizationException(NoAuthorizationException e) {
        DefaultErrorType errorType = new DefaultErrorType(ErrorCode.NO_AUTHORIZATION);
        return new ErrorResponse(errorType)
                .responseEntity();
    }

    @ExceptionHandler(NoPrefixJwtException.class)
    public ResponseEntity<ErrorResponse> handleNoPrefixJwtException(NoPrefixJwtException e) {
        DefaultErrorType errorType = new DefaultErrorType(ErrorCode.JWT_NO_PREFIX);
        return new ErrorResponse(errorType)
                .responseEntity();
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ErrorResponse> handleMalformedJwtException(MalformedJwtException e) {
        DefaultErrorType errorType = new DefaultErrorType(ErrorCode.JWT_MALFORMED);
        return new ErrorResponse(errorType)
                .responseEntity();
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<ErrorResponse> handleUnsupportedJwtException(UnsupportedJwtException e) {
        DefaultErrorType errorType = new DefaultErrorType(ErrorCode.JWT_UNSUPPORTED);
        return new ErrorResponse(errorType)
                .responseEntity();
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ErrorResponse> handleSignatureException(SignatureException e) {
        DefaultErrorType errorType = new DefaultErrorType(ErrorCode.JWT_SIG_INVALID);
        return new ErrorResponse(errorType)
                .responseEntity();
    }
}
