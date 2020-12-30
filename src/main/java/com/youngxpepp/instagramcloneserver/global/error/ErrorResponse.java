package com.youngxpepp.instagramcloneserver.global.error;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

import com.youngxpepp.instagramcloneserver.global.error.type.DefaultErrorType;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class ErrorResponse {

	private DefaultErrorType error;

	public ErrorResponse(DefaultErrorType error) {
		this.error = error;
	}

	public ResponseEntity<ErrorResponse> responseEntity() {
		return new ResponseEntity<>(this, this.error.getHttpStatus());
	}
}
