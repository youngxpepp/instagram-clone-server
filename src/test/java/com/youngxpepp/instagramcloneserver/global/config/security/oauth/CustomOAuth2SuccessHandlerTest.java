package com.youngxpepp.instagramcloneserver.global.config.security.oauth;

import static org.assertj.core.api.BDDAssertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.youngxpepp.instagramcloneserver.dao.MemberRepository;
import com.youngxpepp.instagramcloneserver.test.MockTest;

public class CustomOAuth2SuccessHandlerTest extends MockTest {

	@Mock
	private OAuth2AdditionalStateRepository<OAuth2StateInfo> oAuth2StateInfoRepository;

	@Mock
	private MemberRepository memberRepository;

	@InjectMocks
	private CustomOAuth2SuccessHandler customOAuth2SuccessHandler;

	@ParameterizedTest
	@ValueSource(strings = {"http://localhost:8080", "http://localhost:8080/api/v1"})
	public void givenAbsoluteUrlWhenGetRedirectUrlThenSuccess(String url) throws
		NoSuchMethodException,
		InvocationTargetException,
		IllegalAccessException {
		// given
		String sessionId = "abcdefghijklmnopqrstuvwxyz";
		Method method = customOAuth2SuccessHandler.getClass()
			.getDeclaredMethod("getRedirectUrl", String.class, String.class);
		method.setAccessible(true);

		// when
		String result = (String)method.invoke(customOAuth2SuccessHandler, url, sessionId);

		// then
		then(result).isEqualTo(url + "?sessionId=" + sessionId);
	}

	@ParameterizedTest
	@ValueSource(strings = {"/", "/login"})
	public void givenNotAbsoluteUrlWhenGetRedirectUrlThenSuccess(String url) throws
		NoSuchMethodException,
		InvocationTargetException,
		IllegalAccessException {
		// given
		String sessionId = "abcdefghijklmnopqrstuvwxyz";
		Method method = customOAuth2SuccessHandler.getClass()
			.getDeclaredMethod("getRedirectUrl", String.class, String.class);
		method.setAccessible(true);

		// when
		String result = (String)method.invoke(customOAuth2SuccessHandler, url, sessionId);

		// then
		then(result).isEqualTo(url);
	}

}
