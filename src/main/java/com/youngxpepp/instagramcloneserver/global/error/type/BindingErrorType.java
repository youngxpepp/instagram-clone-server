package com.youngxpepp.instagramcloneserver.global.error.type;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import org.springframework.validation.BindingResult;

import com.youngxpepp.instagramcloneserver.global.error.ErrorCode;

@Getter
public class BindingErrorType extends DefaultErrorType {

	public BindingErrorType(ErrorCode errorCode) {
		super(errorCode);
	}

	public BindingErrorType(ErrorCode errorCode, BindingResult bindingResult) {
		this(errorCode);
		super.details.addAll(BindingDetails.of(bindingResult));
	}

	public BindingErrorType(ErrorCode errorCode, String message) {
		super(errorCode, message);
	}

	public BindingErrorType(ErrorCode errorCode, String message, BindingResult bindingResult) {
		this(errorCode, message);
		super.details.addAll(BindingDetails.of(bindingResult));
	}

	@Getter
	public static class BindingDetails {
		private String field;
		private String value;
		private String reason;

		private BindingDetails(String field, String value, String reason) {
			this.field = field;
			this.value = value;
			this.reason = reason;
		}

		public static List<BindingDetails> of(String field, String value, String reason) {
			return Arrays.asList(new BindingDetails(field, value, reason));
		}

		public static List<BindingDetails> of(BindingResult bindingResult) {
			return bindingResult.getFieldErrors().stream()
				.map(error -> new BindingDetails(
					error.getField(),
					error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
					error.getDefaultMessage()
				)).collect(Collectors.toList());
		}
	}
}
