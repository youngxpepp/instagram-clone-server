package com.youngxpepp.instagramcloneserver.service;

import java.util.Arrays;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.youngxpepp.instagramcloneserver.dao.ArticleRepository;
import com.youngxpepp.instagramcloneserver.dao.FollowRepository;
import com.youngxpepp.instagramcloneserver.domain.MemberOAuth2Info;
import com.youngxpepp.instagramcloneserver.dto.GetMemberResponseBody;
import com.youngxpepp.instagramcloneserver.dto.MemberDto;
import com.youngxpepp.instagramcloneserver.dto.SignupDto;
import com.youngxpepp.instagramcloneserver.global.config.security.oauth.AuthenticatedAuthenticationToken;
import com.youngxpepp.instagramcloneserver.mapper.MemberMapper;
import com.youngxpepp.instagramcloneserver.domain.Member;
import com.youngxpepp.instagramcloneserver.domain.MemberRole;
import com.youngxpepp.instagramcloneserver.dao.MemberRepository;
import com.youngxpepp.instagramcloneserver.global.error.ErrorCode;
import com.youngxpepp.instagramcloneserver.global.error.exception.BusinessException;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final FollowRepository followRepository;
	private final ArticleRepository articleRepository;
	private final MemberMapper memberMapper;

	@Transactional
	public GetMemberResponseBody getMember(Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

		Long followerCount = followRepository.countByFollowedMemberId(member.getId());
		Long followingCount = followRepository.countByFollowingMemberId(member.getId());
		Long articleCount = articleRepository.countByCreatedById(member.getId());

		return memberMapper.toGetMemberResponseBody(member, followerCount, followingCount, articleCount);
	}

	@Transactional
	public MemberDto signup(SignupDto dto) {
		Member member = memberMapper.toMember(dto, MemberRole.MEMBER);
		MemberOAuth2Info oauth2Info = MemberOAuth2Info.builder()
			.name(dto.getOauth2NameAttribute())
			.registrationId(dto.getOauth2RegistrationId())
			.member(member)
			.build();
		member.setOauth2Info(oauth2Info);

		memberRepository.save(member);

		SecurityContextHolder
			.getContext()
			.setAuthentication(
				AuthenticatedAuthenticationToken.from(member)
			);

		return memberMapper.toMemberDto(member);
	}
}
