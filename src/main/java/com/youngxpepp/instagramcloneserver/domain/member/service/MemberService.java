package com.youngxpepp.instagramcloneserver.domain.member.service;

import java.util.Arrays;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.youngxpepp.instagramcloneserver.domain.follow.repository.FollowRepository;
import com.youngxpepp.instagramcloneserver.domain.member.dto.GetMemberResponseDto;
import com.youngxpepp.instagramcloneserver.domain.member.dto.SignupRequestDto;
import com.youngxpepp.instagramcloneserver.domain.member.model.Google;
import com.youngxpepp.instagramcloneserver.domain.member.model.Member;
import com.youngxpepp.instagramcloneserver.domain.member.model.MemberRole;
import com.youngxpepp.instagramcloneserver.domain.member.repository.GoogleRepository;
import com.youngxpepp.instagramcloneserver.domain.member.repository.MemberRepository;
import com.youngxpepp.instagramcloneserver.global.config.security.jwt.AccessTokenClaims;
import com.youngxpepp.instagramcloneserver.global.config.security.jwt.JwtUtils;
import com.youngxpepp.instagramcloneserver.global.error.ErrorCode;
import com.youngxpepp.instagramcloneserver.global.error.exception.BusinessException;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final FollowRepository followRepository;
	private final GoogleRepository googleRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtils jwtUtils;

	@Transactional
	public GetMemberResponseDto getMember(Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

		Long followerCount = followRepository.countByToMemberId(member.getId());
		Long followingCount = followRepository.countByFromMemberId(member.getId());

		return GetMemberResponseDto.builder()
			.id(member.getId())
			.name(member.getName())
			.nickname(member.getNickname())
			.followerCount(followerCount)
			.followingCount(followingCount)
			.build();
	}

	public String login(String nickname, String password) {
		Member member = memberRepository.findByNickname(nickname)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

		if (!this.passwordEncoder.matches(password, member.getPassword())) {
			throw new BusinessException(ErrorCode.WRONG_PASSWORD);
		}

		AccessTokenClaims accessTokenClaims = AccessTokenClaims.builder()
			.memberId(member.getId())
			.roles(Arrays.asList(member.getRole()))
			.build();
		String accessToken = jwtUtils.generateAccessToken(accessTokenClaims);
		return accessToken;
	}

	@Transactional
	public String signup(String key, SignupRequestDto requestDto) {
		memberRepository.findByNickname(requestDto.getNickname())
			.ifPresent(member -> new BusinessException(ErrorCode.ENTITY_ALREADY_EXIST));
		googleRepository.findByKey(key)
			.ifPresent(google -> new BusinessException(ErrorCode.ENTITY_ALREADY_EXIST));

		Member member = Member.builder()
			.name(requestDto.getName())
			.nickname(requestDto.getNickname())
			.password(passwordEncoder.encode(requestDto.getPassword()))
			.role(MemberRole.MEMBER)
			.build();
		memberRepository.save(member);

		Google google = new Google(member, key);
		googleRepository.save(google);

		AccessTokenClaims claims = AccessTokenClaims.builder()
			.memberId(member.getId())
			.roles(Arrays.asList(member.getRole()))
			.build();
		String accessToken = jwtUtils.generateAccessToken(claims);

		return accessToken;
	}
}
