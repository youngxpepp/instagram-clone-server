package com.youngxpepp.instagramcloneserver.global.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum ErrorCode {

    INVALID_INPUT_VALUE(1000, HttpStatus.BAD_REQUEST, "Invalid input value"),
    METHOD_NOT_ALLOWED(1001, HttpStatus.METHOD_NOT_ALLOWED, "Method is not allowed"),
    ENTITY_NOT_FOUND(1002, HttpStatus.BAD_REQUEST, "Entity is not found"),
    INTERNAL_SERVER_ERROR(1003, HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    INVALID_TYPE_VALUE(1004, HttpStatus.BAD_REQUEST, "Invalid type value"),
    HANDLE_ACCESS_DENIED(1005, HttpStatus.FORBIDDEN, "Access is denied"),

//    Authentication
    AUTHENTICATION_FAILED(2000, HttpStatus.BAD_REQUEST, "Authentication is failed"),
    JWT_EXPIRED(2001, HttpStatus.BAD_REQUEST, "JsonWebToken is expired"),
    JWT_PREFIX_NOT_FOUND(2002, HttpStatus.BAD_REQUEST, "JsonWebToken must have a prefix Bearer");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    ErrorCode(int code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public static ErrorCode resolve(int code) {
        for(ErrorCode element : ErrorCode.values()) {
            if(element.getCode() == code) {
                return element;
            }
        }
        return null;
    }
}
