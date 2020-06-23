package com.youngxpepp.instagramcloneserver.global.error;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ErrorResponse {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private List<FieldError> fieldErrors;
    private String code;

    public ErrorResponse(ErrorCode errorCode) {
        this.timestamp = LocalDateTime.now();
        this.status = errorCode.getHttpStatus().value();
        this.error = errorCode.getHttpStatus().getReasonPhrase();
        this.message = errorCode.getMessage();
        this.fieldErrors = new ArrayList<>();
        this.code = errorCode.getCode();
    }

    public ErrorResponse(ErrorCode errorCode, BindingResult bindingResult) {
        this(errorCode);
        this.fieldErrors = FieldError.of(bindingResult);
    }

    public ErrorResponse(ErrorCode errorCode, List<FieldError> fieldErrors) {
        this(errorCode);
        this.fieldErrors = fieldErrors;
    }

    public ErrorResponse(MethodArgumentTypeMismatchException e) {
        this(ErrorCode.INVALID_TYPE_VALUE);
        String value = e.getValue() == null ? "" : e.getValue().toString();
        this.fieldErrors.addAll(FieldError.of(e.getName(), value, e.getErrorCode()));
    }

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Getter
    public static class FieldError {

        private String field;
        private String value;
        private String reason;

        private FieldError(String field, String value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        public static List<FieldError> of(String field, String value, String reason) {
            List<FieldError> fieldErrors = new ArrayList<>();
            fieldErrors.add(new FieldError(field, value, reason));
            return fieldErrors;
        }

        public static List<FieldError> of(BindingResult bindingResult) {
            return bindingResult.getFieldErrors().stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()
                    )).collect(Collectors.toList());
        }
    }
}
