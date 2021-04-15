package com.youngxpepp.instagramcloneserver.test;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.youngxpepp.instagramcloneserver.dao.MemberRepository;
import com.youngxpepp.instagramcloneserver.domain.Member;
import com.youngxpepp.instagramcloneserver.global.config.security.login.MemberDetails;

@RequiredArgsConstructor
@Slf4j
public class WithCustomSecurityContextFactory implements WithSecurityContextFactory<WithCustomSecurityContext> {

	private final MemberRepository memberRepository;

	@Override
	public SecurityContext createSecurityContext(WithCustomSecurityContext annotation) {
		Member member = memberRepository.findByNickname(annotation.nickname())
			.orElse(null);
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		Authentication authentication = null;
		if (member != null) {
			MemberDetails memberDetails = MemberDetails.from(member);
			authentication = new UsernamePasswordAuthenticationToken(
				memberDetails,
				memberDetails.getPassword(),
				memberDetails.getAuthorities()
			);
		}
		context.setAuthentication(authentication);
		return context;
	}
}
