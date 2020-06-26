package com.youngxpepp.instagramcloneserver.global.error.exception;

import com.youngxpepp.instagramcloneserver.global.error.ErrorCode;

public class BusinessException extends RuntimeException {

    private ErrorCode errorCode;

//    public BusinessException(String message) {
//        super(message);
//    }
//
//    public BusinessException(String message, ErrorCode errorCode) {
//        super(message);
//        this.errorCode = errorCode;
//    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
