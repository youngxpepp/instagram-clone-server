package com.youngxpepp.instagramcloneserver.domain.follow.service;

import javax.validation.Valid;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.youngxpepp.instagramcloneserver.domain.follow.dto.FollowRequestDto;
import com.youngxpepp.instagramcloneserver.domain.follow.dto.UnfollowRequestDto;
import com.youngxpepp.instagramcloneserver.domain.follow.model.Follow;
import com.youngxpepp.instagramcloneserver.domain.follow.repository.FollowRepository;
import com.youngxpepp.instagramcloneserver.domain.member.model.Member;
import com.youngxpepp.instagramcloneserver.domain.member.repository.MemberRepository;
import com.youngxpepp.instagramcloneserver.global.error.ErrorCode;
import com.youngxpepp.instagramcloneserver.global.error.exception.BusinessException;

@Service
@AllArgsConstructor
@Slf4j
public class FollowService {

	private FollowRepository followRepository;
	private MemberRepository memberRepository;

	@Transactional
	public void follow(Long memberId, FollowRequestDto dto) throws BusinessException {
		Member fromMember = memberRepository.getOne(memberId);
		Member toMember = memberRepository.findByNickname(dto.getMemberNickname())
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

		followRepository.findByFromMemberIdAndToMemberId(fromMember.getId(), toMember.getId())
			.ifPresent(follow -> {
				throw new BusinessException(ErrorCode.ENTITY_ALREADY_EXIST);
			});

		Follow follow = Follow.builder()
			.fromMember(fromMember)
			.toMember(toMember)
			.build();

		followRepository.save(follow);
	}

	@Transactional
	public void unfollow(Long memberId, @Valid UnfollowRequestDto dto) {
		Member fromMember = memberRepository.getOne(memberId);
		Member toMember = memberRepository.findByNickname(dto.getMemberNickname())
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

		Follow follow = followRepository.findByFromMemberIdAndToMemberId(fromMember.getId(), toMember.getId())
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

		followRepository.delete(follow);
	}
}
