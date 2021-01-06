package com.youngxpepp.instagramcloneserver.domain.follow.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.youngxpepp.instagramcloneserver.domain.follow.dto.FollowDto;
import com.youngxpepp.instagramcloneserver.domain.follow.dto.FollowMapper;
import com.youngxpepp.instagramcloneserver.domain.follow.model.Follow;
import com.youngxpepp.instagramcloneserver.domain.follow.repository.FollowRepository;
import com.youngxpepp.instagramcloneserver.domain.member.model.Member;
import com.youngxpepp.instagramcloneserver.domain.member.repository.MemberRepository;
import com.youngxpepp.instagramcloneserver.global.error.ErrorCode;
import com.youngxpepp.instagramcloneserver.global.error.exception.BusinessException;

@Service
@RequiredArgsConstructor
public class FollowService {

	private final FollowRepository followRepository;
	private final MemberRepository memberRepository;
	private final FollowMapper followMapper;


	@Transactional
	public FollowDto follow(Long followingMemberId, Long followedMemberId) {
		Member followingMember = memberRepository.findById(followingMemberId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
		Member followedMember = memberRepository.findById(followedMemberId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

		followRepository.findByFollowingMemberIdAndFollowedMemberId(followingMember.getId(), followedMember.getId())
			.ifPresent(follow -> {
				throw new BusinessException(ErrorCode.ENTITY_ALREADY_EXIST);
			});

		Follow follow = Follow.builder()
			.followingMember(followingMember)
			.followedMember(followedMember)
			.build();

		followRepository.save(follow);

		return followMapper.toFollowDto(follow);
	}

	@Transactional
	public void unfollow(Long followingMemberId, Long followId) {
		Follow follow = followRepository.findById(followId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

		if (!follow.getFollowingMember().getId().equals(followingMemberId)) {
			throw new BusinessException(ErrorCode.HANDLE_ACCESS_DENIED);
		}

		followRepository.delete(follow);
	}
}
