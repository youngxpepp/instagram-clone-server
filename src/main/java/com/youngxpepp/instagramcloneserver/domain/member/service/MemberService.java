package com.youngxpepp.instagramcloneserver.domain.member.service;

import java.util.Arrays;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.youngxpepp.instagramcloneserver.domain.follow.repository.FollowRepository;
import com.youngxpepp.instagramcloneserver.domain.member.dto.MemberServiceDto;
import com.youngxpepp.instagramcloneserver.domain.member.model.Member;
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
	private final PasswordEncoder passwordEncoder;
	private final JwtUtils jwtUtils;

	@Transactional
	public MemberServiceDto.GetMemberResponseDto getMember(String memberNickname) {
		Member member = memberRepository.findByNickname(memberNickname)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

		Long followerCount = followRepository.getFollowerCountByMemberNickname(memberNickname);
		Long followingCount = followRepository.getFollowingCountByMemberNickname(memberNickname);

		return MemberServiceDto.GetMemberResponseDto.builder()
			.memberNickname(member.getNickname())
			.memberName(member.getName())
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
}
