package com.youngxpepp.instagramcloneserver.global.error.exception;

import io.jsonwebtoken.JwtException;

public class NoPrefixJwtException extends JwtException {

    public NoPrefixJwtException(String message) {
        super(message);
    }
}
