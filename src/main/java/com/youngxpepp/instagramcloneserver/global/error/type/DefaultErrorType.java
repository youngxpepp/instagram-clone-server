package com.youngxpepp.instagramcloneserver.global.error.type;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import com.youngxpepp.instagramcloneserver.global.error.ErrorCode;

@Getter
public class DefaultErrorType {

	@Setter
	protected List<Object> details = new ArrayList<>();
	private int code;
	private String message;
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
