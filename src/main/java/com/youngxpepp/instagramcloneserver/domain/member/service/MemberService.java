package com.youngxpepp.instagramcloneserver.domain.member.service;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.youngxpepp.instagramcloneserver.domain.follow.model.Follow;
import com.youngxpepp.instagramcloneserver.domain.member.dto.MemberServiceDto;
import com.youngxpepp.instagramcloneserver.domain.member.model.Member;
import com.youngxpepp.instagramcloneserver.domain.member.repository.MemberRepository;
import com.youngxpepp.instagramcloneserver.global.error.ErrorCode;
import com.youngxpepp.instagramcloneserver.global.error.exception.BusinessException;

@Service
@AllArgsConstructor
public class MemberService {

	private MemberRepository memberRepository;

	@Transactional
	public MemberServiceDto.GetMemberResponseDto getMember(String memberNickname) {
		Member member = memberRepository.findByNickname(memberNickname)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

		member.getFollowersSize();
		member.getFollowingsSize();

		return MemberServiceDto.GetMemberResponseDto.builder()
			.memberNickname(member.getNickname())
			.memberName(member.getName())
			.memberEmail(member.getEmail())
			.followerCount(member.getFollowersSize())
			.followingCount(member.getFollowingsSize())
			.build();
	}
}
