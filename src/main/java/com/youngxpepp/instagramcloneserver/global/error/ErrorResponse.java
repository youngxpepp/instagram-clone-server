package com.youngxpepp.instagramcloneserver.global.error;

import com.youngxpepp.instagramcloneserver.global.error.type.DefaultErrorType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

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
