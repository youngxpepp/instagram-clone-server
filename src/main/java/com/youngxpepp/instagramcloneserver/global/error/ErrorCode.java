package com.youngxpepp.instagramcloneserver.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

	INVALID_INPUT_VALUE(1000, HttpStatus.BAD_REQUEST, "Invalid input value"),
	METHOD_NOT_ALLOWED(1001, HttpStatus.METHOD_NOT_ALLOWED, "Method is not allowed"),
	ENTITY_NOT_FOUND(1002, HttpStatus.NOT_FOUND, "Entity is not found"),
	INTERNAL_SERVER_ERROR(1003, HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
	INVALID_TYPE_VALUE(1004, HttpStatus.BAD_REQUEST, "Invalid type value"),
	HANDLE_ACCESS_DENIED(1005, HttpStatus.FORBIDDEN, "Access is denied"),
	ENTITY_ALREADY_EXIST(1006, HttpStatus.BAD_REQUEST, "Entity already exists"),

	// Authentication
	AUTHENTICATION_FAILED(2000, HttpStatus.BAD_REQUEST, "Authentication is failed"),
	JWT_EXPIRED(2001, HttpStatus.BAD_REQUEST, "JsonWebToken is expired"),
	NO_AUTHORIZATION(2003, HttpStatus.BAD_REQUEST, "No authorization in header"),
	JWT_NO_PREFIX(2004, HttpStatus.BAD_REQUEST, "No prefix in jwt"),
	JWT_MALFORMED(2005, HttpStatus.BAD_REQUEST, "JsonWebToken is malformed"),
	JWT_SIG_INVALID(2006, HttpStatus.BAD_REQUEST, "JsonWebToken signature is invalid"),
	JWT_UNSUPPORTED(2007, HttpStatus.BAD_REQUEST, "JsonWebToken format is unsupported"),
	JWT_EXCEPTION(2008, HttpStatus.BAD_REQUEST, "JsonWebToken has a problem"),
	WRONG_PASSWORD(2009, HttpStatus.BAD_REQUEST, "Password is wrong");

	private final int code;
	private final HttpStatus httpStatus;
	private final String message;

	ErrorCode(int code, HttpStatus httpStatus, String message) {
		this.code = code;
		this.httpStatus = httpStatus;
		this.message = message;
	}

	public static ErrorCode resolve(int code) {
		for (ErrorCode element : ErrorCode.values()) {
			if (element.getCode() == code) {
				return element;
			}
		}
		return null;
	}
}
