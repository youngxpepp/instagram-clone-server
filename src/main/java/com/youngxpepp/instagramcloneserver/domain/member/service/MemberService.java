package com.youngxpepp.instagramcloneserver.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.youngxpepp.instagramcloneserver.domain.follow.repository.FollowRepository;
import com.youngxpepp.instagramcloneserver.domain.member.dto.GetMemberResponseDto;
import com.youngxpepp.instagramcloneserver.domain.member.dto.MemberDto;
import com.youngxpepp.instagramcloneserver.domain.member.dto.MemberMapper;
import com.youngxpepp.instagramcloneserver.domain.member.dto.SignupRequestBody;
import com.youngxpepp.instagramcloneserver.domain.member.dto.SignupResponseBody;
import com.youngxpepp.instagramcloneserver.domain.member.model.Member;
import com.youngxpepp.instagramcloneserver.domain.member.repository.MemberRepository;
import com.youngxpepp.instagramcloneserver.global.error.ErrorCode;
import com.youngxpepp.instagramcloneserver.global.error.exception.BusinessException;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final FollowRepository followRepository;
	private final MemberMapper memberMapper;

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
	public SignupResponseBody signup(MemberDto memberDto) {
		memberRepository.findByEmail(memberDto.getEmail())
			.ifPresent(member -> {
				throw new BusinessException(ErrorCode.ENTITY_ALREADY_EXIST);
			});
		memberRepository.findByNickname(memberDto.getNickname())
			.ifPresent(member -> {
				throw new BusinessException(ErrorCode.ENTITY_ALREADY_EXIST);
			});

		Member member = memberMapper.toMemberEntity(memberDto);
		memberRepository.save(member);

		return memberMapper.toSignupResponseBody(member);
	}
}
