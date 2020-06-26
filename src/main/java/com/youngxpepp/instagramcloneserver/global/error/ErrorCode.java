package com.youngxpepp.instagramcloneserver.global.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum ErrorCode {

    INVALID_INPUT_VALUE("00000", HttpStatus.BAD_REQUEST, "Invalid Input Value"),
    METHOD_NOT_ALLOWED("00001", HttpStatus.METHOD_NOT_ALLOWED, "Invalid Input Value"),
    ENTITY_NOT_FOUND("00002", HttpStatus.BAD_REQUEST, "Entity Not Found"),
    INTERNAL_SERVER_ERROR("00003", HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
    INVALID_TYPE_VALUE("00004", HttpStatus.BAD_REQUEST, "Invalid Type Value"),
    HANDLE_ACCESS_DENIED("00005", HttpStatus.FORBIDDEN, "Access Is Denied"),

//    JWT
    EXPIRED_JWT("JWT-00001", HttpStatus.BAD_REQUEST, "JsonWebToken Is Expired");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(String code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
