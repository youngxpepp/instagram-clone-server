package com.youngxpepp.instagramcloneserver.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import com.youngxpepp.instagramcloneserver.domain.Member;
import com.youngxpepp.instagramcloneserver.domain.MemberRole;
import com.youngxpepp.instagramcloneserver.dto.GetMemberResponseBody;
import com.youngxpepp.instagramcloneserver.test.IntegrationTest;
import com.youngxpepp.instagramcloneserver.test.WithCustomSecurityContext;

public class MemberIntegrationTest extends IntegrationTest {

	@Test
	@WithCustomSecurityContext(nickname = "youngxpepp")
	public void getMeThenSuccess() throws Exception {
		// given
		GetMemberResponseBody responseBody = new GetMemberResponseBody(
			principal.getId(),
			principal.getName(),
			principal.getNickname(),
			principal.getProfileImageUrl(),
			principal.getDescription(),
			0L,
			0L,
			0L
		);
		String responseBodyAsString = objectMapper.writeValueAsString(responseBody);

		// when
		// then
		mockMvc.perform(get("/api/v1/members/me").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().string(responseBodyAsString));
	}

	@Test
	@WithCustomSecurityContext(nickname = "youngxpepp")
	public void getMemberThenSuccess() throws Exception {
		// given
		Member target = Member.builder()
			.email("lky4697@gmail.com")
			.nickname("lky")
			.name("kangyeop")
			.profileImageUrl("https://naver.com")
			.description("this is a description")
			.role(MemberRole.MEMBER)
			.build();
		em.persist(target);

		GetMemberResponseBody responseBody = new GetMemberResponseBody(
			target.getId(),
			target.getName(),
			target.getNickname(),
			target.getProfileImageUrl(),
			target.getDescription(),
			0L,
			0L,
			0L
		);
		String responseBodyAsString = objectMapper.writeValueAsString(responseBody);

		// when
		// then
		mockMvc.perform(get("/api/v1/members/{memberId}", target.getId())
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().string(responseBodyAsString));
	}
}
