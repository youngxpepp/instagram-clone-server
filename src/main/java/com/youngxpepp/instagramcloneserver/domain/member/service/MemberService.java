package com.youngxpepp.instagramcloneserver.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.youngxpepp.instagramcloneserver.domain.follow.repository.FollowRepository;
import com.youngxpepp.instagramcloneserver.domain.member.dto.GetMemberResponseDto;
import com.youngxpepp.instagramcloneserver.domain.member.dto.MemberDto;
import com.youngxpepp.instagramcloneserver.domain.member.dto.SignupRequestDto;
import com.youngxpepp.instagramcloneserver.domain.member.model.Member;
import com.youngxpepp.instagramcloneserver.domain.member.model.MemberRole;
import com.youngxpepp.instagramcloneserver.domain.member.repository.MemberRepository;
import com.youngxpepp.instagramcloneserver.global.error.ErrorCode;
import com.youngxpepp.instagramcloneserver.global.error.exception.BusinessException;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final FollowRepository followRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public GetMemberResponseDto getMember(Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

		Long followerCount = followRepository.countByFollowedMemberId(member.getId());
		Long followingCount = followRepository.countByFollowingMemberId(member.getId());

		return GetMemberResponseDto.builder()
			.id(member.getId())
			.name(member.getName())
			.nickname(member.getNickname())
			.followerCount(followerCount)
			.followingCount(followingCount)
			.build();
	}

	@Transactional
	public MemberDto signup(String email, SignupRequestDto requestDto) {
		memberRepository.findByEmail(email)
			.ifPresent(member -> {
				throw new BusinessException(ErrorCode.ENTITY_ALREADY_EXIST);
			});
		memberRepository.findByNickname(requestDto.getNickname())
			.ifPresent(member -> {
				throw new BusinessException(ErrorCode.ENTITY_ALREADY_EXIST);
			});

		Member member = Member.builder()
			.email(email)
			.name(requestDto.getName())
			.nickname(requestDto.getNickname())
			.password(passwordEncoder.encode(requestDto.getPassword()))
			.role(MemberRole.MEMBER)
			.build();
		memberRepository.save(member);

		return MemberDto.ofMember(member);
	}
}
