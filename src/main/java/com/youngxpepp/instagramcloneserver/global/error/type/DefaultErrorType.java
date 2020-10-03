package com.youngxpepp.instagramcloneserver.global.error.type;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import com.youngxpepp.instagramcloneserver.global.error.ErrorCode;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class DefaultErrorType {

	protected List<Object> details = new ArrayList<>();

	private Integer code;

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
