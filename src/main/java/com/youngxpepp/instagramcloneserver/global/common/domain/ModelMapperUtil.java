package com.youngxpepp.instagramcloneserver.global.common.domain;

import org.modelmapper.ModelMapper;

public class ModelMapperUtil {
	private static final ModelMapper MODEL_MAPPER = new ModelMapper();

	public static <D> D mapClass(Object source, Class<D> type) {
		return source != null ? MODEL_MAPPER.map(source, type) : null;
	}

	private ModelMapperUtil() {
		// blank
	}
}
