package com.youngxpepp.instagramcloneserver.global.error.exception;

import org.springframework.security.core.AuthenticationException;

public class NoAuthorizationException extends AuthenticationException {

    public NoAuthorizationException(String message) {
        super(message);
    }
}
