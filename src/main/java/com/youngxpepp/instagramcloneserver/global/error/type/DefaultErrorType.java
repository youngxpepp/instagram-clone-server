package com.youngxpepp.instagramcloneserver.global.error.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.youngxpepp.instagramcloneserver.global.error.ErrorCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DefaultErrorType {

    private int code;
    private String message;

    @Setter
    protected List<Object> details = new ArrayList<>();

    @JsonIgnore
    private HttpStatus httpStatus;

    public DefaultErrorType(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.httpStatus = errorCode.getHttpStatus();
    }

    public DefaultErrorType(ErrorCode errorCode, String message) {
        this(errorCode);
        this.message = message;
    }

}
