package com.youngxpepp.instagramcloneserver.global.error;

import lombok.Getter;
import org.springframework.http.ResponseEntity;

import com.youngxpepp.instagramcloneserver.global.error.type.DefaultErrorType;

@Getter
public class ErrorResponse {

	private DefaultErrorType error;

	public ErrorResponse(DefaultErrorType error) {
		this.error = error;
	}

	public ResponseEntity<ErrorResponse> responseEntity() {
		return new ResponseEntity<>(this, this.error.getHttpStatus());
	}
}
